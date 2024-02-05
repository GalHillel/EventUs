import { HttpException, HttpStatus, Injectable, NotFoundException } from '@nestjs/common';
import { InjectModel } from '@nestjs/mongoose';
import { Model } from 'mongoose';
import { User, userDisplayFields } from './user.model';
import { UserEvent } from '../event/event.model';
import { ProfilePic } from '../profilePic/profilePic.model';

import { CreateUserDto, EditUserDto, LoginUserDto } from '../dto/user.dto';
import { Id } from '../dto/id.dto';
import { Message } from '../message/message.model';
import { CreateMessageDto } from '../dto/message.dto';


@Injectable()
export class UserService {
  
  constructor(
    @InjectModel(User.name) private readonly userModel: Model<User>,
  ) {}


  /**
   * Adds a new user to the database if there is no user that shares the same email and is of the same type
   * @param createUserDto DTO containing registration information
   * @returns user object from the database 
   */
  async createUser(createUserDto: CreateUserDto): Promise<User> {
    
    if(await this.userModel.findOne({email:createUserDto.email, user_type:createUserDto.user_type}).exec() != null){
      throw new HttpException(createUserDto.user_type+" with this email already exists!",HttpStatus.CONFLICT)
    }
    console.log("creating user" + createUserDto);
    const createdUser = new this.userModel(createUserDto);
    return createdUser.save();
  }


  /**
   * Gets a user with matching email, password, and user_type from the database 
   * @param loginUserDto DTO containing email, password and user_type
   * @returns user object from the database with only the user display fields or null if doesn't exist
   */
  async loginUser(loginUserDto: LoginUserDto): Promise<User>{
    if(Object.keys(loginUserDto).length === 0){
      throw new HttpException("Empty!",HttpStatus.NOT_ACCEPTABLE)
    }
    return this.userModel.findOne(loginUserDto,userDisplayFields).exec().then((user)=>{
      if(user == null){
        throw new HttpException("Incorrect credentials!",HttpStatus.FORBIDDEN)
      }
      return user;
    })
    
  }


  async findAllUsers(): Promise<User[]> {
    return this.userModel.find().exec();
  }
  async printAllUsers(): Promise<void>{
    const users: User[] = await this.userModel.find().exec();
    
    users.forEach( (user,index)=>{
      console.log(user);
    });
  }


  // TODO: error handling
  /**
   * Get all users by ids
   * @param _ids List of _id field of desired users
   * @param fields get only selected fields from each user
   * @returns List of desired users
   */
  async getUsers(_ids:Id[],fields?:string): Promise<User[]>{
    return await this.userModel.find({ _id: { $in: _ids } },fields).exec();
  }
  /**
   * Get user by user Id
   * @param _id _id field of the desired user
   * @param field get only selected fields from user
   * @returns Desired user
   */
  async getUser(_id:Id, field?:string): Promise<User>{
    return this.userModel.findById(_id,field).exec().then((user) => { 
      if (!user) throw new NotFoundException('User '+_id+' not Found');
      return user;
    })
  }


  async getEventIds(_id: Id): Promise<Id[]> {
    return (await this.getUser(_id,'events')).events; 
  }
  async getProfilePicId(_id: Id): Promise<Id> {
    return (await this.getUser(_id,'profile_pic')).profile_pic;
  }
  async getMessageIds(_id: Id): Promise<Id[]> {
    return (await this.getUser(_id,'messages')).messages;
  }

  
  /**
   * Adds an event id to the users events list
   * @param _id user _id
   * @param eventId event _id
   * @returns updated user
   */
  async addEvent(_id:Id,eventId:Id): Promise<User>{
    const user = await this.getUser(_id);
    //dupe check
    if (user.events.includes(eventId)){
      throw new HttpException("Event exists for user!",HttpStatus.CONFLICT);
    } 
    user.events.push(eventId);
    await user.save();
    return user;
  }

  /**
   * removes an event id from the users events list
   * @param _id user _id
   * @param eventId event _id
   * @returns updated user
   */
  async removeEvent(_id:Id,eventId:Id): Promise<void>{
    await this.userModel.findById(_id).updateOne({},{ $pull: {events: eventId} }).exec();
  }

  async editUser(_id: Id, edit: EditUserDto) {
    const user = await this.getUser(_id,"user_type password");

    if(edit.password != null){
      if(edit.oldPassword == null){
        throw new HttpException("No old password provided!",HttpStatus.FAILED_DEPENDENCY)
      }
      else{
        if(edit.oldPassword != user.password){
          throw new HttpException("Wrong password!",HttpStatus.FAILED_DEPENDENCY)
        }
      }
    }
    if(edit.email != null){
      if(await this.userModel.findOne({email:edit.email, user_type:user.user_type}).exec() != null){
        throw new HttpException(user.user_type+" with this email already exists!",HttpStatus.CONFLICT)
      }
    }

    this.userModel.updateOne(_id,edit).exec();
    
  }


  /**
   * Adds a message to all users inbox's, there is no check for duplicate messages
   * @param userIds user id's
   * @param msgId msg id
   */
  async addMessages(userIds: Id[],msgId:Id): Promise<void>{
    //no need to check for duplicate messages as this function gets called only on message creation
    await this.userModel.find({ _id: { $in: userIds } }).updateMany({},{ $push: {messages: msgId} }).exec()
  }
  



 
  

  // Implement other CRUD operations as needed
}