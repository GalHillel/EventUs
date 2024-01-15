import { Prop, Schema, SchemaFactory } from '@nestjs/mongoose';
import { Document } from 'mongoose';
import { Id } from '../dto/id.dto';


@Schema()
export class UserEvent extends Document {    
    
    @Prop()
    _id: Id;
    
    @Prop({ required: true })
    name: string;
    
    @Prop({ required: true })
    date: Date;
    
    @Prop({ default: "TBD" })
    location: string;
    
    @Prop({ default: "" })
    description: string;
    
    @Prop({ required: true })
    creator_id: Id;
    
    @Prop({ default: [] })
    attendents: Id[];
}

export const EventSchema = SchemaFactory.createForClass(UserEvent);