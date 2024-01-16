
import { Injectable, NotFoundException } from '@nestjs/common';
import { InjectModel } from '@nestjs/mongoose';
import { Model } from 'mongoose';
import { UserEvent } from './event.model';
import { CreateEventDto } from '../dto/event.dto';

import { User } from '../user/user.model';
import { Id } from '../dto/id.dto';

@Injectable()
export class EventService {
  constructor(
    @InjectModel(UserEvent.name) private readonly userEventModel: Model<UserEvent>,
    @InjectModel(User.name) private readonly userModel: Model<User>
  ) {}

  async createEvent(createEventDto: CreateEventDto): Promise<UserEvent> {
    console.log("creating event" + createEventDto);
    const createdEvent = new this.userEventModel(createEventDto);
    return createdEvent.save();
  }

  async findAllEvents(): Promise<UserEvent[]> {
    return this.userEventModel.find().exec();
  }
  async printAllEvents(): Promise<void>{
    const events: UserEvent[] = await this.userEventModel.find().exec();
    
    events.forEach( (event,index)=>{
      console.log(event);
    });
  }

  async getUsersForEvent(_id:Id): Promise<User[]>{
    const userEvent = await this.userEventModel.findById(_id).exec();
    
    if (!userEvent) {
      throw new NotFoundException('User not found');
    }

    const userIds = userEvent.attendents; 
    if(userIds.length>0){
      return await this.userModel.find({ _id: { $in: userIds } }).exec();
    }
    return [];
  }

  // Implement other CRUD operations as needed
}
