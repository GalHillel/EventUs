import { Injectable, NotFoundException } from '@nestjs/common';
import { InjectModel } from '@nestjs/mongoose';
import { Model } from 'mongoose';
import { ProfilePic } from './profilePic.model';
import { CreateProfilePicDto } from '../dto/profilePic.dto';
import { Id } from '../dto/id.dto';

@Injectable()
export class ProfilePicService {
  constructor(@InjectModel(ProfilePic.name) private readonly profilePicModel: Model<ProfilePic>) {}

  async createProfilePic(createProfilePicDto:CreateProfilePicDto): Promise<ProfilePic> {
    
    const profilePic =  new this.profilePicModel(createProfilePicDto);
    return profilePic.save();
  }

  async findAllProfilePics(): Promise<ProfilePic[]> {
    return this.profilePicModel.find().exec();
  }
  async printAllProfilePics(): Promise<void>{
    const profilepics: ProfilePic[] = await this.profilePicModel.find().exec();
    
    profilepics.forEach( (profilepic,index)=>{
      
      console.log(profilepic);
    });
  }

  async getProfilePic(_id:Id, field?:string): Promise<ProfilePic>{
    return this.profilePicModel.findById(_id,field).exec().then((pic) => { 
        if (!pic) throw new NotFoundException('profile pic '+_id+' not Found');
        return pic;
      }
    )
  }
  async getIcon(_id:Id): Promise<Buffer>{
    
    return (await this.getProfilePic(_id,'icon')).icon; 
  }

  // Implement other CRUD operations as needed
}