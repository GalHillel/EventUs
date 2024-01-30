import { Prop, Schema, SchemaFactory } from '@nestjs/mongoose';
import { Document, Schema as mongooseSchema } from 'mongoose';
import { Id } from '../dto/id.dto';

@Schema()
export class User extends Document {    
    
    @Prop()
    profile_pic: Id;
    
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
    messages: Id[];
    
    //@Prop({ type: [{ type: Id, ref: 'userevents' }], default: [] })
    @Prop({ default: [] })
    events: Id[];


}

export const UserSchema = SchemaFactory.createForClass(User);