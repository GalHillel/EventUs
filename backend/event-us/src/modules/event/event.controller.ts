import { Controller, Post, Body, Get, Param, Patch, Delete, Query, HttpCode } from '@nestjs/common';
import { EventService } from './event.service';
import { CreateEventDto, editEventDto, searchEventDto } from '../dto/event.dto';
import { UserEvent } from './event.model';
import {Id} from '../dto/id.dto'
import { User, userDisplayFields } from '../user/user.model';
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

  @Get("event/:id")
  async getEventInfo(@Param("id") _id: Id): Promise<UserEvent> {
    
    return this.eventService.getUserEvent(_id);
  }
  @Get("search")
  async searchEvent(@Query() searchTerms: searchEventDto): Promise<UserEvent[]>{
    
    return this.eventService.search(searchTerms);
  }
  
  /**
   * events/<event id>/users, returns a list of users
   * @param _id event id
   * @returns list of users with only display user fields
   */
  @Get(":id/users")
  async getEventUsers(@Param("id") _id: Id): Promise<User[]>{
    return  this.eventService.getUserIds(_id).then((ids) => this.userService.getUsers(ids,userDisplayFields))
  }

  @Get(":id/creator")
  async getEventCreator_id(@Param("id") _id: Id): Promise<User>{
    return  this.eventService.getCreator_id(_id).then((creator) => this.userService.getUser(creator,userDisplayFields))
  }
  
  /**
   * events/<event id>/joinEvent, patch request should contain a json in the form {_id:<user id>}
   * @param _id event id
   * @param userId user id
   */
  @HttpCode(204)
  @Patch(':id/joinEvent')
  async joinEvent(@Param('id') _id: Id, @Body('_id') userId: Id): Promise<void>{
    
    this.userService.addEvent(userId, _id);
    this.eventService.addUser(_id,userId);
  }

  /**
   * events/<event id>/edit, patch request should contain a json with the new fields to be updated
   * @param _id 
   * @param edit 
   */
  @HttpCode(204)
  @Patch(':id/edit')
  async editEvent(@Param('id') _id: Id, @Body() edit:editEventDto): Promise<void>{
    return this.eventService.editEvent(_id,edit);
  }


  @Delete(':id')
  async delEvent(@Param('id') _id:Id){
    const userIds = await this.eventService.getUserIds(_id)
    this.eventService.deleteEvent(_id);
    userIds.forEach((userId) => this.userService.removeEvent(userId,_id))
  }

  // Implement other CRUD endpoints as needed
}