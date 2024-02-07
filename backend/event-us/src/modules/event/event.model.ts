import { Prop, Schema, SchemaFactory } from '@nestjs/mongoose';
import { Document } from 'mongoose';
//import { string } from '../dto/id.dto';



@Schema()
export class UserEvent extends Document {    
    
    @Prop({ required: true })
    name: string;
    
    @Prop({ required: true })
    date: Date;
    
    @Prop({ default: "TBD" })
    location: string;
    
    @Prop({ default: "" })
    description: string;
    
    @Prop({ required: true })
    creator_id: string;
    
    @Prop({ default: {} })
    attendents: Map<string,boolean>;

    @Prop({default: 0})
    rating: number;

    @Prop({default: 0})
    num_ratings: number;

    @Prop({default: false})
    isPrivate: boolean;
}

export const userEventDisplayFields = "_id name date location"

export const UserEventSchema = SchemaFactory.createForClass(UserEvent);