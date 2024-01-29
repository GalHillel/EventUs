import {Id} from './id.dto'

export enum UserType {USER="user", CREATOR="creator"};
export class CreateUserDto {
    
    readonly profile_pic: Id;
    readonly name: string;
    readonly email: string;
    readonly password: string;
    readonly user_type: UserType;
    readonly messages: Id[];
    readonly events: Id[];

  }