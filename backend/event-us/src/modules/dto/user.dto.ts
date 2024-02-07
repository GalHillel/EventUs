import { OmitType, PartialType, PickType } from '@nestjs/swagger';
//import {string} from './id.dto'


export class UserDto{
  readonly _id: string;
  readonly name: string;
  readonly email: string;
  readonly password: string;
  readonly user_type: string;
  readonly profile_pic: string;
  readonly messages: string[];
  readonly events: string[];
}
export class CreateUserDto extends OmitType(UserDto,['_id','profile_pic','messages','events']){}
export class LoginUserDto extends PickType(UserDto,['email','password','user_type']){}
export class SearchUserDto extends PickType(UserDto,['_id','name','email','user_type']){}
export class EditUserDto extends PickType(UserDto,['name','email','password','profile_pic']){
  readonly oldPassword: string;
}

  