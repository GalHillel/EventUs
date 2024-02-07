import { IntersectionType, OmitType, PartialType, PickType } from '@nestjs/swagger';

import { Expose } from 'class-transformer';

export class EventDto{
  readonly _id: string;
  readonly name: string;
  readonly date: Date;
  readonly location: string;
  readonly description: string;
  readonly creator_id: string;
  readonly attendents: string[];
}


export class CreateEventDto extends OmitType(EventDto,['_id','attendents']){}

export class SearchEventDto extends PickType(EventDto,['_id','name']){}

export class EditEventDto extends OmitType(CreateEventDto, ['creator_id']){}