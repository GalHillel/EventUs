import { Controller, Post, Body, Get, Param, ValidationPipe, UseInterceptors,ClassSerializerInterceptor, Query } from '@nestjs/common';
import { UserService } from './user.service';
import { CreateUserDto } from '../dto/user.dto';
import { User } from './user.model';
import { UserEvent } from '../event/event.model';
import {Id} from '../dto/id.dto'
import { Message } from '../message/message.model';

@Controller('users')
export class UserController {
  constructor(private readonly userService: UserService) {}

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
    return this.userService.getEventsForUser(_id);
  }
  @Get(':id/profilepics')
  async getUserProfilePic(@Param('id') _id: Id): Promise<Buffer>{
    return this.userService.getProfilePicForUser(_id);
  }
  @Get(':id/messages')
  async getUserMessages(@Param('id') _id: Id): Promise<Message[]>{
    return this.userService.getMessagesForUser(_id);
  }

  // Implement other CRUD endpoints as needed
}