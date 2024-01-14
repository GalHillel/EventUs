import {id} from './id.dto'

export class CreateUserDto {
    readonly _id: id;
    readonly name: string;
    readonly email: string;
    readonly password: string;

  }