import { Prop, Schema, SchemaFactory } from '@nestjs/mongoose';
import { Document } from 'mongoose';
import { Id } from '../dto/id.dto';

@Schema()
export class ProfilePic extends Document {    
    @Prop()
    _id: Id;
    
    @Prop({ required: true })
    icon: Buffer;
    

}

export const ProfilePicSchema = SchemaFactory.createForClass(ProfilePic);