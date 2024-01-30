import {Id} from './id.dto'

export class CreateUserDto {
    readonly name: string;
    readonly email: string;
    readonly password: string;
    readonly user_type: string;
  }
  export class LoginUserDto {
    readonly email: string;
    readonly password: string;
    readonly user_type: string;
  }