//import {string} from './id.dto'

export class CreateMessageDto {
    
    readonly sender_id: string;
    readonly receiver_ids: string[];
    readonly title: string;
    readonly content: string;
    readonly date_sent: Date; 
  }