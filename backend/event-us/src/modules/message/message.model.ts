import { Prop, Schema, SchemaFactory } from '@nestjs/mongoose';
import { Schema as MongoScheme } from "mongoose";
import { Document } from 'mongoose';
import { Schema as mongooseSchema } from "mongoose";
import { Id } from '../dto/id.dto';



@Schema()
export class Message extends Document {    
    
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