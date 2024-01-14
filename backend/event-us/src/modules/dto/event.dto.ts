import {id} from './id.dto'
export class CreateUserEvent{
    readonly _id: id;
    readonly name: string;
    readonly date: Date;
    readonly location: string;
    readonly description: string;
    readonly creator_id: id;
    readonly attendents: id[];
    

  }