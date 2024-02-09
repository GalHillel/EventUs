//import {string} from './id.dto'

import { OmitType } from "@nestjs/swagger";

export class MessageDto{
  readonly _id: string
  readonly sender_id: string;
  readonly receiver_ids: string[];
  readonly title: string;
  readonly content: string;
  readonly date_sent: Date; 
}

export class CreateMessageDto extends OmitType(MessageDto,['_id','date_sent']){}
export class SearchMessageDto extends OmitType(MessageDto,['title','content']){}