import { Controller, Post, Body, Get, Param, ValidationPipe, UseInterceptors,ClassSerializerInterceptor, Query } from '@nestjs/common';
import { UserService } from './user.service';
import { CreateUserDto } from '../dto/user.dto';
import { User } from './user.model';
import { UserEvent } from '../event/event.model';
import {Id} from '../dto/id.dto'
import { Message } from '../message/message.model';
import { EventService } from '../event/event.service';
import { ProfilePicService } from '../profilePic/profilePic.service';
import { MessageService } from '../message/message.service';

@Controller('users')
export class UserController {
  constructor(
    private readonly userService: UserService,
    private readonly eventService: EventService,
    private readonly profilePicService: ProfilePicService,
    private readonly messageService: MessageService,
  ) {}

  @Post()
  async createUser(@Body() createUserDto: CreateUserDto): Promise<User> {
    return this.userService.createUser(createUserDto);
  }

  @Get()
  async findAllUsers(): Promise<User[]> {
    this.userService.printAllUsers();
    return this.userService.findAllUsers();
  }


  // get user specific fields  
  @Get(':id/events')
  async getUserEvents(@Param('id') _id: Id): Promise<UserEvent[]>{
    return this.userService.getEventIds(_id).then((ids) => this.eventService.getUserEvents(ids));
  }
  @Get(':id/profilepic')
  async getUserProfilePicIcon(@Param('id') _id: Id): Promise<Buffer>{
    return this.userService.getProfilePic(_id).then((profile_id) => this.profilePicService.getIcon(profile_id));
  }
  @Get(':id/messages')
  async getUserMessages(@Param('id') _id: Id): Promise<Message[]>{
    return this.userService.getEventIds(_id).then((ids) => this.messageService.getMessages(ids));
  }

  // Implement other CRUD endpoints as needed
}