import { Controller, Post, Body, Get } from '@nestjs/common';
import { EventService } from './event.service';
import { CreateEventDto } from '../dto/event.dto';
import { UserEvent } from './event.model';

@Controller('events')
export class EventController {
  constructor(private readonly eventService: EventService) {}

  @Post()
  async createEvent(@Body() createEventDto: CreateEventDto): Promise<UserEvent> {
    return this.eventService.createEvent(createEventDto);
  }

  @Get()
  async findAllEvents(): Promise<UserEvent[]> {
    this.eventService.printAllEvents();
    return this.eventService.findAllEvents();
  }

  // Implement other CRUD endpoints as needed
}