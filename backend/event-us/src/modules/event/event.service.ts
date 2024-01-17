
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

  /**
   * Get message by message Id
   * @param _id _id field of the desired message
   * @returns Desired message
   */
  async getUserEvent(_id:Id,field?:string): Promise<UserEvent>{
    return this.userEventModel.findById(_id,field).exec().then((message) => { 
      if (!message) throw new NotFoundException('Event '+_id+' not Found');
      return message;
    });
  }
  // TODO: error handling
  /**
   * Get all messages by ids
   * @param _ids List of _id field of desired messages
   * @returns List of desired messages
   */
  async getUserEvents(_ids:Id[]): Promise<UserEvent[]>{
    return this.userEventModel.find({ _id: { $in: _ids } }).exec();
  }
  async getUserIds(_id: Id): Promise<Id[]> {
    return (await this.getUserEvent(_id,'attendents')).attendents; 
  }
  async getCreator_id(_id: Id): Promise<Id> {
    return (await this.getUserEvent(_id,'creator_id')).creator_id; 
  }

  // Implement other CRUD operations as needed
}
