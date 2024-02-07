import { Controller, Post, Body, Get, Param, ValidationPipe, UseInterceptors,ClassSerializerInterceptor, Query, Put, Patch, ParseUUIDPipe, HttpException, HttpStatus, HttpCode, UsePipes } from '@nestjs/common';
import { UserService } from './user.service';
import { CreateUserDto, EditUserDto, LoginUserDto, SearchUserDto } from '../dto/user.dto';
import { User, userDisplayFields } from './user.model';
import { UserEvent, userEventDisplayFields } from '../event/event.model';
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

  @Post("register")
  async createUser(@Body() createUserDto: CreateUserDto): Promise<User> {
    try{
      return await this.userService.createUser(createUserDto);
    } catch(e){
      if(e instanceof HttpException)
        throw new HttpException(e.message,e.getStatus());
    }

  }
  
  /**
   * users, get a list of users matching the search terms
   * @param searchTerms 
   * @returns list of users with full details
   */
  @Get()
  @UsePipes(new ValidationPipe({ transform: true }))
  async findAllUsers(@Query() searchTerms: SearchUserDto): Promise<User[]> {
    return this.userService.search(searchTerms);
  }

  /**
   * users/search, get a list of only the display fields of users matching the search terms
   * @param searchTerms 
   * @returns list of users with partial details
   */
  @Get("search")
  @UsePipes(new ValidationPipe({ transform: true }))
  async searchEvent(@Query() searchTerms: SearchUserDto): Promise<User[]>{
    return this.userService.search(searchTerms,userDisplayFields);
  }

  /**
   * users/<user id>/profile, get full profile details by id
   * @param _id 
   * @returns full details of one user
   */
  @Get(":id/profile")
  async getEventInfo(@Param('id') _id: string): Promise<UserEvent> {
    return this.eventService.getUserEvent(_id);
  }

  /**
   * users/login
   * @param loginUserDto dict containing {email:<user email>, password:<user password>, user_type:<Organizer or Participant>}
   * @returns user on success (200), http error otherwise
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
  async getUserEvents(@Param('id') _id: string): Promise<UserEvent[]>{
    return this.userService.getEventIds(_id).then((ids) => this.eventService.getUserEvents(ids,userEventDisplayFields));
  }
  @Get(':id/profilepic')
  async getUserProfilePicIcon(@Param('id') _id: string): Promise<Buffer>{
    return this.userService.getProfilePicId(_id).then((profile_id) => this.profilePicService.getIcon(profile_id));
  }
  @Get(':id/messages')
  async getUserMessages(@Param('id') _id: string): Promise<Message[]>{
    return this.userService.getMessageIds(_id).then((ids) => this.messageService.getMessages(ids));
  }

  
  /** TODO error handling
   * users/<user id>/exitEvent, Patch request should contain a json in the form {_id:<event id>}
   * @param _id user id
   * @param eventId event id
   */
  @HttpCode(HttpStatus.NO_CONTENT)
  @Patch(':id/exitEvent')
  async exitEvent(@Param('id') _id: string,  @Body('_id') eventId: string): Promise<void>{
    
    await this.eventService.removeUser(eventId,_id);
    await this.userService.removeEvent(_id,eventId);

  }

  /** TODO add guard for event creator
   * users/<user id>/edit, patch request should contain a json with the new fields to be updated
   * @param _id user id
   * @param eventId event id
   */
  @HttpCode(HttpStatus.NO_CONTENT)
  @Patch(':id/edit')
  @UsePipes(new ValidationPipe({ transform: true }))
  async editUser(@Query() _id:string, @Body() editUserDto:EditUserDto): Promise<void>{
    
    try{
      await this.userService.editUser(_id,editUserDto);

    } catch(e){
      if(e instanceof HttpException){
        
        throw new HttpException(e.message,e.getStatus());
      }
        
    }

  }


  // Implement other CRUD endpoints as needed
}