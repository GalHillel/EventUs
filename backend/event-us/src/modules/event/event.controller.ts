import { Controller, Post, Body, Get, Param, Patch } from '@nestjs/common';
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
    const userEvent = await this.eventService.createEvent(createEventDto);
    this.userService.addEvent(createEventDto.creator_id,createEventDto._id)
    return userEvent
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


  /**
   * events/<event id>/joinEvent, Put request should contain a json in the form {_id:<user id>}
   * @param _id event id
   * @param userId user id
   * @returns updated event
   */
  @Patch(':id/joinEvent')
  async joinEvent(@Param('id') _id: Id, @Body('_id') userId: Id): Promise<UserEvent>{
    await this.userService.addEvent(userId,_id);
    return this.eventService.addUser(_id,userId);
  }

  // Implement other CRUD endpoints as needed
}