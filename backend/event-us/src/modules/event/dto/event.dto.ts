export class CreateUserEvent{
    readonly id: number;
    readonly name: string;
    readonly date: Date;
    readonly location: string;
    readonly description: string;
    readonly creator_id: number;
    readonly attendents: number[];
  }