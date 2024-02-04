import { Controller, Post, Body, Get, Param, Patch, Delete } from '@nestjs/common';
import { EventService } from './event.service';
import { CreateEventDto } from '../dto/event.dto';
import { UserEvent } from './event.model';
import {Id} from '../dto/id.dto'
import { User } from '../user/user.model';
import { UserService } from '../user/user.service';
import { Schema as mongooseSchema } from "mongoose";


@Controller('events')
export class EventController {
  constructor(private readonly eventService: EventService,private readonly userService: UserService) {}

  @Post()
  async createEvent(@Body() createEventDto: CreateEventDto): Promise<UserEvent> {
    const userEvent = await this.eventService.createEvent(createEventDto);
    this.userService.addEvent(createEventDto.creator_id,userEvent.id)
    return userEvent
  }


  @Get()
  async findAllEvents(): Promise<UserEvent[]> {
    this.eventService.printAllEvents();
    return this.eventService.findAllEvents();
  }

  @Get(":id")
  async getEventInfo(@Param("id") _id: Id): Promise<UserEvent> {
    
    return this.eventService.getUserEvent(_id);
  }
  
  @Get(":id/users")
  async getEventUsers(@Param("id") _id: Id): Promise<User[]>{
    return  this.eventService.getUserIds(_id).then((ids) => this.userService.getUsers(ids))
  }
  @Get(":id/creator")
  async getEventCreator_id(@Param("id") _id: Id): Promise<User>{
    return  this.eventService.getCreator_id(_id).then((creator) => this.userService.getUser(creator))
  }
  
  /**
   * events/<event id>/joinEvent, Put request should contain a json in the form {_id:<user id>}
   * @param _id event id
   * @param userId user id
   */
  @Patch(':id/joinEvent')
  async joinEvent(@Param('id') _id: Id, @Body('_id') userId: Id): Promise<void>{
    
    this.userService.addEvent(userId, _id);
    this.eventService.addUser(_id,userId);
  }
  
  @Delete(':id')
  async delEvent(@Param('id') _id:Id){
    const userIds = await this.eventService.getUserIds(_id)
    this.eventService.deleteEvent(_id);
    userIds.forEach((userId) => this.userService.removeEvent(userId,_id))
  }

  // Implement other CRUD endpoints as needed
}