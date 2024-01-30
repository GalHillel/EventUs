import {Id} from './id.dto'

export class CreateUserDto {
    
    readonly profile_pic: Id;
    readonly name: string;
    readonly email: string;
    readonly password: string;
    readonly user_type: string;
    readonly messages: Id[];
    readonly events: Id[];

  }
  export class LoginUserDto {
    readonly email: string;
    readonly password: string;
    readonly user_type: string;
  }