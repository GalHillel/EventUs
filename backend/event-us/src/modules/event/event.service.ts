
import { HttpException, HttpStatus, Injectable, NotFoundException } from '@nestjs/common';
import { InjectModel } from '@nestjs/mongoose';
import { Model } from 'mongoose';
import { UserEvent } from './event.model';
import { CreateEventDto, searchEventDto } from '../dto/event.dto';
import { ObjectId } from 'mongoose';

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
    createdEvent.attendents.push(createdEvent.creator_id)
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
   * Get event by event Id
   * @param _id _id field of the desired event
   * @param field get only selected fields from event
   * @returns Desired event
   */
  async getUserEvent(_id:Id,field?:string): Promise<UserEvent>{
    return this.userEventModel.findById(_id,field).exec().then((userEvent) => { 
      if (!userEvent) throw new NotFoundException('Event '+_id+' not Found');
      return userEvent;
    });
  }
  // TODO: error handling
  /**
   * Get all event by ids
   * @param _ids List of _id field of desired event
   * @returns List of desired event
   */
  async getUserEvents(_ids:Id[],fields?:string): Promise<UserEvent[]>{
    return this.userEventModel.find({ _id: { $in: _ids } },fields).exec();
  }
  
  async getUserIds(_id: Id): Promise<Id[]> {
    return (await this.getUserEvent(_id,'attendents')).attendents; 
  }
  async getCreator_id(_id: Id): Promise<Id> {
    return (await this.getUserEvent(_id,'creator_id')).creator_id; 
  }


  async search(searchTerms: searchEventDto): Promise<UserEvent[]>{
    console.log(searchTerms);
    return null;
    //return this.userEventModel.find(searchTerms).exec();
  }

  /**
   * Adds a user id to the events attendents list
   * @param _id event _id
   * @param userId user _id
   * @returns updated event
   */
  async addUser(_id:Id,userId:Id): Promise<UserEvent>{
    const userEvent = await this.getUserEvent(_id);
    //dupe check
    if (userEvent.attendents.includes(userId)){
      throw new HttpException("User exists in event!",HttpStatus.CONFLICT);
    } 
    userEvent.attendents.push(userId);
    await userEvent.save();
    return userEvent;
  }
  /**
   * Removes a user from the events attendents list
   * @param _id event _id
   * @param userId user _id
   */
  async removeUser(_id:Id,userId:Id): Promise<void>{
    
    await this.userEventModel.findOne({ _id: _id }).updateOne({},{ $pull: {attendents: userId} }).exec();
   
  }


  /**
   * Deletes event from database
   * @param _id event id
   */
  async deleteEvent(_id:Id): Promise<void>{
    await this.userEventModel.findById(_id).deleteOne().exec();
  }

  // Implement other CRUD operations as needed
}
