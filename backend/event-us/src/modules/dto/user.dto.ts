import { OmitType, PartialType, PickType } from '@nestjs/swagger';


export class UserDto{
  readonly _id: string;
  readonly name: string;
  readonly email: string;
  readonly password: string;
  readonly bio: string;
  readonly user_type: string;
  readonly profile_pic: string;
  readonly messages: Map<string,boolean>;
  readonly events: string[];
  readonly rating: number;
  readonly num_ratings: number;
}

export class ParticipantDto extends OmitType(UserDto,['rating','num_ratings']){}{}
export class CreateUserDto extends OmitType(UserDto,['_id','profile_pic','messages','events','rating','num_ratings','bio']){}
export class LoginUserDto extends PickType(UserDto,['email','password','user_type']){}
export class SearchUserDto extends PickType(UserDto,['_id','name','email','user_type']){}
export class EditUserDto extends PickType(UserDto,['name','email','password','profile_pic']){
  readonly oldPassword: string;
}

  