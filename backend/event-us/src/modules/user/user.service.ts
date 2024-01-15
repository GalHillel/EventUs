import { Injectable, NotFoundException } from '@nestjs/common';
import { InjectModel } from '@nestjs/mongoose';
import { Model } from 'mongoose';
import { User } from './user.model';
import { UserEvent } from '../event/event.model';
import { CreateUserDto } from '../dto/user.dto';
import { Id } from '../dto/id.dto'

@Injectable()
export class UserService {
  constructor(
    @InjectModel(User.name) private readonly userModel: Model<User>,
    @InjectModel(UserEvent.name) private readonly eventModel: Model<UserEvent>
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

  async getEventsForUser(_id: Id): Promise<UserEvent[]> {
    const user = await this.userModel.findById(_id).exec();

    if (!user) {
      throw new NotFoundException('User not found');
    }

    const eventIds = user.events; 

    const events = await this.eventModel.find({ _id: { $in: eventIds } }).exec();

    return events;
  }

  // Implement other CRUD operations as needed
}