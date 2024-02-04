import { IntersectionType, OmitType, PartialType, PickType } from '@nestjs/swagger';
import {Id} from './id.dto'

export class EventDto{
  readonly _id: string;
  readonly name: string;
  readonly date: Date;
  readonly location: string;
  readonly description: string;
  readonly creator_id: Id;
  readonly attendents: Id[];
}

export class CreateEventDto extends OmitType(EventDto,['_id','attendents']){}

export class searchEventDto{
  readonly name: string;
}

export class editEventDto extends OmitType(CreateEventDto, ['creator_id']){}