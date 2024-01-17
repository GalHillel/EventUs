import { Controller, Post, Body, Get, Param } from '@nestjs/common';
import { EventService } from './event.service';
import { CreateEventDto } from '../dto/event.dto';
import { UserEvent } from './event.model';
import {Id} from '../dto/id.dto'
import { User } from '../user/user.model';
import { UserService } from '../user/user.service';

@Controller('events')
export class EventController {
  constructor(private readonly eventService: EventService,private readonly userService: UserService) {}

  @Post()
  async createEvent(@Body() createEventDto: CreateEventDto): Promise<UserEvent> {
    return this.eventService.createEvent(createEventDto);
  }

  @Get()
  async findAllEvents(): Promise<UserEvent[]> {
    this.eventService.printAllEvents();
    return this.eventService.findAllEvents();
  }
  
  @Get(":id/users")
  async getEventUsers(@Param("id") _id: Id): Promise<User[]>{
    return  this.eventService.getUserIds(_id).then((ids) => this.userService.getUsers(ids))
  }
  @Get(":id/creator")
  async getEventCreator_id(@Param("id") _id: Id): Promise<User>{
    return  this.eventService.getCreator_id(_id).then((creator) => this.userService.getUser(creator))
  }

  // Implement other CRUD endpoints as needed
}