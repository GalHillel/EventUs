import { Prop, Schema, SchemaFactory } from '@nestjs/mongoose';
import { Document } from 'mongoose';
import { Id } from '../dto/id.dto';
import { UserType } from '../dto/user.dto';

@Schema()
export class User extends Document {    
    @Prop()
    _id: Id;
    
    @Prop({ default: 0})
    pfp_id: Id;
    
    @Prop({ required: true })
    name: string;
    
    @Prop({ required: true })
    email: string;
    
    @Prop({ required: true })
    password: string;
    
    @Prop({ required: true })
    userType: UserType;
    
    @Prop({ default: [] })
    messages: Id[];
    
    @Prop({ default: [] })
    events: Id[];

}

export const UserSchema = SchemaFactory.createForClass(User);