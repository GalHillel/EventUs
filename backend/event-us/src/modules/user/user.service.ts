import { Injectable, NotFoundException } from '@nestjs/common';
import { InjectModel } from '@nestjs/mongoose';
import { Model } from 'mongoose';
import { User } from './user.model';
import { UserEvent } from '../event/event.model';
import { ProfilePic } from '../profilePic/profilePic.model';

import { CreateUserDto } from '../dto/user.dto';
import { Id } from '../dto/id.dto'


@Injectable()
export class UserService {
  constructor(
    @InjectModel(User.name) private readonly userModel: Model<User>,
    @InjectModel(UserEvent.name) private readonly userEventModel: Model<UserEvent>,
    @InjectModel(ProfilePic.name) private readonly profilePicModel: Model<ProfilePic>
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

  async getEventsForUser(_id: string): Promise<UserEvent[]> {
    const user = await this.userModel.findById(_id).exec();

    if (!user) {
      throw new NotFoundException('User not found');
    }

    const eventIds = user.events; 
    if(eventIds.length>0){
      return await this.userEventModel.find({ _id: { $in: eventIds } }).exec();
    }
    return [];
  }

  async getProfilePicForUser(_id: string): Promise<Buffer> {
    const user = await this.userModel.findOne({'_id':_id}).exec();
    console.log(typeof _id)
    if (!user) {
      throw new NotFoundException('User not found');
    }
    
    const profilePic = await this.profilePicModel.findById(user.pfp_id).exec();
    if (!profilePic) {
      throw new NotFoundException('profile pic not found');
    }

    return profilePic.icon;
  }

  // Implement other CRUD operations as needed
}