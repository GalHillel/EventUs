
import { Injectable } from '@nestjs/common';
import { InjectModel } from '@nestjs/mongoose';
import { Model } from 'mongoose';
import { UserEvent } from './event.model';
import { CreateEventDto } from '../dto/event.dto';

@Injectable()
export class EventService {
  constructor(@InjectModel(UserEvent.name) private readonly eventModel: Model<UserEvent>) {}

  async createEvent(createEventDto: CreateEventDto): Promise<UserEvent> {
    console.log("creating event" + createEventDto);
    const createdEvent = new this.eventModel(createEventDto);
    return createdEvent.save();
  }

  async findAllEvents(): Promise<UserEvent[]> {
    return this.eventModel.find().exec();
  }
  async printAllEvents(): Promise<void>{
    const events: UserEvent[] = await this.eventModel.find().exec();
    
    events.forEach( (event,index)=>{
      console.log(event);
    });
  }

  // Implement other CRUD operations as needed
}
