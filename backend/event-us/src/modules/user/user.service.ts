import { HttpException, HttpStatus, Injectable, NotFoundException } from '@nestjs/common';
import { InjectModel } from '@nestjs/mongoose';
import { Model } from 'mongoose';
import { User } from './user.model';
import { UserEvent } from '../event/event.model';
import { ProfilePic } from '../profilePic/profilePic.model';

import { CreateUserDto } from '../dto/user.dto';
import { Id } from '../dto/id.dto'
import { Message } from '../message/message.model';
import { CreateMessageDto } from '../dto/message.dto';


@Injectable()
export class UserService {
  constructor(
    @InjectModel(User.name) private readonly userModel: Model<User>,
    ) {}

  async createUser(createUserDto: CreateUserDto): Promise<User> {
    console.log("creating user" + createUserDto);
    const createdUser = new this.userModel(createUserDto);
    return createdUser.save();
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
   * @returns List of desired users
   */
  async getUsers(_ids:Id[]): Promise<User[]>{
    return await this.userModel.find({ _id: { $in: _ids } }).exec();
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
    
    return (await this.getUser(_id,'profilePic')).profilePic;
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