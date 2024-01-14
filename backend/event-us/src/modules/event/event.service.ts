
import { Injectable } from '@nestjs/common';
import { InjectModel } from '@nestjs/mongoose';
import { Model } from 'mongoose';
import { Event } from './event.model';
import { CreateEventDto } from '../dto/event.dto';

@Injectable()
export class EventService {
  constructor(@InjectModel(Event.name) private readonly eventModel: Model<Event>) {}

  async createEvent(createEventDto: CreateEventDto): Promise<Event> {
    console.log("creating event" + createEventDto);
    const createdEvent = new this.eventModel(createEventDto);
    return createdEvent.save();
  }

  async findAllEvents(): Promise<Event[]> {
    return this.eventModel.find().exec();
  }
  async printAllEvents(): Promise<void>{
    const events: Event[] = await this.eventModel.find().exec();
    
    events.forEach( (event,index)=>{
      console.log(event);
    });
  }

  // Implement other CRUD operations as needed
}
