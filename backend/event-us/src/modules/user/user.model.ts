import { Prop, Schema, SchemaFactory } from '@nestjs/mongoose';
import { Document, Schema as mongooseSchema } from 'mongoose';
//import { string } from '../dto/id.dto';

@Schema()
export class User extends Document {    
    
    @Prop({default: ""})
    profile_pic: string;
    
    @Prop({ required: true })
    name: string;
    
    @Prop({ required: true })
    email: string;
    
    @Prop({ required: true })
    password: string;
    
    @Prop({ required: true })
    user_type: string;
    
    //@Prop({ type: [{ type: Id, ref: 'message' }], default: [] })
    @Prop({ default: [] })
    messages: string[];
    
    //@Prop({ type: [{ type: Id, ref: 'userevents' }], default: [] })
    @Prop({ default: [] })
    events: string[];


}

export const userDisplayFields = "_id name user_type"

export const UserSchema = SchemaFactory.createForClass(User);