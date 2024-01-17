import { Controller, Post, Body, Get } from '@nestjs/common';
import { MessageService } from './message.service';
import { CreateMessageDto } from '../dto/message.dto';
import { Message } from './message.model';

@Controller('messages')
export class MessageController {
  constructor(private readonly messageService: MessageService) {}

  @Post()
  async createMessage(@Body() createMessageDto: CreateMessageDto): Promise<Message> {
    return this.messageService.createMessage(createMessageDto);
  }

  @Get()
  async findAllMessages(): Promise<Message[]> {
    this.messageService.printAllMessages();
    return this.messageService.getAllMessages();
  }

  // Implement other CRUD endpoints as needed
}