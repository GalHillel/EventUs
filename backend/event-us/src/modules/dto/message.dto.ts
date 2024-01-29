import {Id} from './id.dto'

export class CreateMessageDto {
    
    readonly sender_id: Id;
    readonly receiver_ids: Id[];
    readonly title: string;
    readonly content: string;
    readonly date_sent: Date; 
  }