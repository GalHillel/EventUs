import { Injectable } from '@nestjs/common';
import { InjectModel } from '@nestjs/mongoose';
import { Model } from 'mongoose';
import { Message } from './message.model';
import { CreateMessageDto } from '../dto/message.dto';

@Injectable()
export class MessageService {
  constructor(@InjectModel(Message.name) private readonly messageModel: Model<Message>) {}

  async createMessage(createMessageDto: CreateMessageDto): Promise<Message> {
    console.log("creating message" + createMessageDto);
    const createdMessage = new this.messageModel(createMessageDto);
    return createdMessage.save();
  }

  async findAllMessages(): Promise<Message[]> {
    return this.messageModel.find().exec();
  }
  async printAllMessages(): Promise<void>{
    const messages: Message[] = await this.messageModel.find().exec();
    
    messages.forEach( (message,index)=>{
      console.log(message);
    });
  }

  // Implement other CRUD operations as needed
}