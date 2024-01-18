import { Controller, Post, Body, Get } from '@nestjs/common';
import { MessageService } from './message.service';
import { CreateMessageDto } from '../dto/message.dto';
import { Message } from './message.model';
import { UserService } from '../user/user.service';

@Controller('messages')
export class MessageController {
  constructor(
    private readonly messageService: MessageService,
    private readonly userService: UserService
  ) {}

  @Post()
  async createMessage(@Body() createMessageDto: CreateMessageDto): Promise<Message> {
    const message = await this.messageService.createMessage(createMessageDto);
    await this.userService.addMessages(message.receiver_ids,message._id)
    return message
  }

  @Get()
  async findAllMessages(): Promise<Message[]> {
    this.messageService.printAllMessages();
    return this.messageService.getAllMessages();
  }

  // Implement other CRUD endpoints as needed
}