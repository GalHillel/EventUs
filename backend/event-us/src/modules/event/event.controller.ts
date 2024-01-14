import { Controller, Post, Body, Get } from '@nestjs/common';
import { EventService } from './event.service';
import { CreateEventDto } from '../dto/event.dto';
import { Event } from './event.model';

@Controller('events')
export class EventController {
  constructor(private readonly eventService: EventService) {}

  @Post()
  async createEvent(@Body() createEventDto: CreateEventDto): Promise<Event> {
    return this.eventService.createEvent(createEventDto);
  }

  @Get()
  async findAllEvents(): Promise<Event[]> {
    this.eventService.printAllEvents();
    return this.eventService.findAllEvents();
  }

  // Implement other CRUD endpoints as needed
}