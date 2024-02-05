import { Controller, Post, Body, Get, Param, ValidationPipe, UseInterceptors,ClassSerializerInterceptor, Query, Put, Patch, ParseUUIDPipe, HttpException, HttpStatus, HttpCode, UsePipes } from '@nestjs/common';
import { UserService } from './user.service';
import { CreateUserDto, EditUserDto, LoginUserDto } from '../dto/user.dto';
import { User } from './user.model';
import { UserEvent, userEventDisplayFields } from '../event/event.model';
import { Id } from '../dto/id.dto'
import { Message } from '../message/message.model';
import { EventService } from '../event/event.service';
import { ProfilePicService } from '../profilePic/profilePic.service';
import { MessageService } from '../message/message.service';
import { CreateMessageDto } from '../dto/message.dto';
import { EditEventDto } from '../dto/event.dto';


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
    try{
      return await this.userService.createUser(createUserDto);
    } catch(e){
      if(e instanceof HttpException)
        throw new HttpException(e.message,e.getStatus());
    }

  }
  

  @Get()
  async findAllUsers(): Promise<User[]> {
    this.userService.printAllUsers();
    return this.userService.findAllUsers();
  }

  /**
   * 
   * @param loginUserDto 
   * @returns 
   */
  @Get("login")
  async login(@Query() loginUserDto: LoginUserDto): Promise<User>{
    console.log(loginUserDto);
    try{

      return await this.userService.loginUser(loginUserDto);

    } catch(e){
      
      if(e instanceof HttpException)
        throw new HttpException(e.message,e.getStatus());
    }
    
  }


  
  @Get(':id/events')
  async getUserEvents(@Param('id') _id: Id): Promise<UserEvent[]>{
    return this.userService.getEventIds(_id).then((ids) => this.eventService.getUserEvents(ids,userEventDisplayFields));
  }
  @Get(':id/profilepic')
  async getUserProfilePicIcon(@Param('id') _id: Id): Promise<Buffer>{
    return this.userService.getProfilePicId(_id).then((profile_id) => this.profilePicService.getIcon(profile_id));
  }
  @Get(':id/messages')
  async getUserMessages(@Param('id') _id: Id): Promise<Message[]>{
    return this.userService.getMessageIds(_id).then((ids) => this.messageService.getMessages(ids));
  }

  /**
   * users/<user id>/joinEvent, Patch request should contain a json in the form {_id:<event id>}
   * @param _id user id
   * @param eventId event id
   * 
   *//*
  @Patch(':id/joinEvent')
  async joinEvent(@Param('id') _id: Id, @Body('_id') eventId: Id): Promise<void>{
    await this.eventService.addUser(eventId,_id);
    await this.userService.addEvent(_id,eventId);
  }*/
  
  /** TODO add guard for event creator
   * users/<user id>/exitEvent, Patch request should contain a json in the form {_id:<event id>}
   * @param _id user id
   * @param eventId event id
   */
  @HttpCode(HttpStatus.NO_CONTENT)
  @Patch(':id/exitEvent')
  async exitEvent(@Param('id') _id: Id,  @Body('_id') eventId: Id): Promise<void>{
    
    await this.eventService.removeUser(eventId,_id);
    await this.userService.removeEvent(_id,eventId);

  }

  /** TODO add guard for event creator
   * users/<user id>/exitEvent, Patch request should contain a json in the form {_id:<event id>}
   * @param _id user id
   * @param eventId event id
   */
  @HttpCode(HttpStatus.NO_CONTENT)
  @Patch('edit')
  @UsePipes(new ValidationPipe({ transform: true }))
  async editUser(@Query() _id:Id, @Body() editUserDto:EditUserDto): Promise<void>{
    
    try{

      await this.userService.editUser(_id,editUserDto);

    } catch(e){
      if(e instanceof HttpException){
        console.log(e.message)
        throw new HttpException(e.message,e.getStatus());
      }
        
    }

  }


  // Implement other CRUD endpoints as needed
}