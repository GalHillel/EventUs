import { Module } from '@nestjs/common';
import { MongooseModule } from '@nestjs/mongoose';
import { UserController } from './user.controller';
import { UserService } from './user.service';
import { User, UserSchema } from './user.model';

import { UserEvent, UserEventSchema } from '../event/event.model';
import { ProfilePic, ProfilePicSchema } from '../profilePic/profilePic.model';
import { Message, MessageSchema } from '../message/message.model';

@Module({
    imports: [MongooseModule.forFeature([
      { name: User.name, schema: UserSchema },
      { name: UserEvent.name, schema: UserEventSchema },
      { name: ProfilePic.name, schema: ProfilePicSchema },
      { name: Message.name, schema: MessageSchema } ])],
    controllers: [UserController],
    providers: [UserService],
  })
export class UserModule {}
