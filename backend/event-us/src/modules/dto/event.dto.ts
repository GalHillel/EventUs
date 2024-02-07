import { IntersectionType, OmitType, PartialType, PickType } from '@nestjs/swagger';

import { Expose } from 'class-transformer';

class userStatus{
  readonly _id:string;
  readonly isAccepted:string;
}

export class EventDto{
  readonly _id: string;
  readonly name: string;
  readonly date: Date;
  readonly location: string;
  readonly description: string;
  readonly creator_id: string;
  readonly attendents: Map<string,boolean>;
  readonly rating: number;
  readonly num_ratings: number;
  readonly isPrivate: boolean;
}


export class CreateEventDto extends OmitType(EventDto,['_id','attendents','rating','num_ratings']){}

export class SearchEventDto extends PickType(EventDto,['_id','name']){}

export class EditEventDto extends OmitType(EventDto, ['_id','creator_id']){}