
import { Injectable } from '@nestjs/common';
import { InjectModel } from '@nestjs/mongoose';
import { Model } from 'mongoose';
import { UserEvent } from './event.model';
import { CreateEventDto } from '../dto/event.dto';

@Injectable()
export class EventService {
  constructor(@InjectModel(UserEvent.name) private readonly userEventModel: Model<UserEvent>) {}

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

  // Implement other CRUD operations as needed
}
