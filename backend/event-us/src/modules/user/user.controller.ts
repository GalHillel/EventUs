import { Controller, Post, Body, Get, Param, ValidationPipe, UseInterceptors,ClassSerializerInterceptor, Query } from '@nestjs/common';
import { UserService } from './user.service';
import { CreateUserDto } from '../dto/user.dto';
import { User } from './user.model';
import { UserEvent } from '../event/event.model';
import {Id} from '../dto/id.dto'

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
  @Get('events')
  async getUserEvents(@Query('id') _id: string): Promise<UserEvent[]>{
    return this.userService.getEventsForUser(_id);
  }
  @Get('profilepics')
  async getUserProfilePic(@Query('id') _id: string): Promise<Buffer>{
    console.log(_id);
    return this.userService.getProfilePicForUser(_id);
  }

  // Implement other CRUD endpoints as needed
}