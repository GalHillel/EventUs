import { Prop, Schema, SchemaFactory } from '@nestjs/mongoose';
import { Document } from 'mongoose';
import { Id } from '../dto/id.dto';

@Schema()
export class Message extends Document {    
    @Prop()
    _id: Id;
    
    @Prop({ required: true })
    sender_id: Id;
    
    @Prop({ required: true })
    receiver_ids: Id[];

    @Prop({ default: "" })
    title: string;
    
    @Prop({ default: "" })
    content: string;
    
    @Prop({ default: Date.now })
    date_sent: Date; 

}

export const MessageSchema = SchemaFactory.createForClass(Message);