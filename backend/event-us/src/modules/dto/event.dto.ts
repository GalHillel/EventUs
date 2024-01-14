import {Id} from './id.dto'
export class CreateUserEvent{
    readonly _id: Id;
    readonly name: string;
    readonly date: Date;
    readonly location: string;
    readonly description: string;
    readonly creator_id: Id;
    readonly attendents: Id[];
    

  }