import { Module } from '@nestjs/common';
import { MongooseModule } from '@nestjs/mongoose';
import { EventController } from './event.controller';
import { EventService } from './event.service';
import { UserEvent, EventSchema } from './event.model';

@Module({
    imports: [MongooseModule.forFeature([{ name: UserEvent.name, schema: EventSchema }])],
    controllers: [EventController],
    providers: [EventService],
  })
export class EventModule {}