import { Injectable, NotFoundException } from '@nestjs/common';
import { InjectModel } from '@nestjs/mongoose';
import { Model } from 'mongoose';
import { Message } from './message.model';
import { CreateMessageDto } from '../dto/message.dto';
import { Id } from '../dto/id.dto';

@Injectable()
export class MessageService {
  constructor(@InjectModel(Message.name) private readonly messageModel: Model<Message>) {}

  async createMessage(createMessageDto: CreateMessageDto): Promise<Message> {
    console.log("creating message" + createMessageDto);
    const createdMessage = new this.messageModel(createMessageDto);
    return createdMessage.save();
  }

  /**
   *
   * @returns List of all messages in the colleciton
   */
  async getAllMessages(): Promise<Message[]> {
    return this.messageModel.find().exec();
  }
  /**
   * Prints all the messages in the collection
   */
  async printAllMessages(): Promise<void>{
    const messages: Message[] = await this.messageModel.find().exec();
    messages.forEach( (message,index)=>{
      console.log(message);
    });
  }

  /**
   * Get message by message Id
   * @param _id _id field of the desired message
   * @returns Desired message
   */
  async getMessage(_id:Id,field?:string): Promise<Message>{
    return this.messageModel.findById(_id,field).exec().then((message) => { 
      if (!message) throw new NotFoundException('Message '+_id+' not Found');
      return message;
    })
    
  }

  // TODO: error handling
  /**
   * Get all messages by ids
   * @param _ids List of _id field of desired messages
   * @returns List of desired messages
   */
  async getMessages(_ids:Id[]): Promise<Message[]>{
    return await this.messageModel.find({ _id: { $in: _ids } }).exec();
  }

  /**
   * Get message contents by message Id
   * @param _id _id field of the desired message
   * @returns contents of desired message
   */
  async getMessageContent(_id:Id): Promise<string>{
    return (await this.getMessage(_id)).content;
  }
  /**
   * Get sender Id by message Id
   * @param _id message id
   * @returns sender Id
   */
  async getSenderId(_id:Id): Promise<Id>{
    return (await this.getMessage(_id)).sender_id;
  }
  

  // Implement other CRUD operations as needed
}