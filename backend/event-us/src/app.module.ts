import { Module } from '@nestjs/common';

import { MongooseModule } from '@nestjs/mongoose';
import { UserModule } from './modules/user/user.module';
import { EventModule } from './modules/event/event.module';
import { MessageModule } from './modules/message/message.module';
import { ProfilePicModule } from './modules/profilePic/profilePic.module';
@Module({
  imports: [ 
    MongooseModule.forRoot('mongodb+srv://zivmorgan:ZfgRRn3HDEj9mC4d@cluster0.c5gbgne.mongodb.net/?retryWrites=true&w=majority/EventUs'), UserModule, EventModule,MessageModule,ProfilePicModule],
  controllers: [],
  providers: [],
})
export class AppModule {}
