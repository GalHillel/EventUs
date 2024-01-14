import { Prop, Schema, SchemaFactory } from '@nestjs/mongoose';
import { Document } from 'mongoose';
import { Id } from '../dto/id.dto';

@Schema()
export class User extends Document {
  
    @Prop()
    _id: Id;

    @Prop({ required: true})
    name: string;

    @Prop({ required: true })
    email: string;

    @Prop({ required: true })
    password: string;
    
    @Prop({ default: Date.now })
    createdAt: Date;

    @Prop({ default: Date.now })
    updatedAt: Date;

}

export const UserSchema = SchemaFactory.createForClass(User);