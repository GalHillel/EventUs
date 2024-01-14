import { Injectable } from '@nestjs/common';
import { InjectModel } from '@nestjs/mongoose';
import { Model } from 'mongoose';
import { ProfilePic } from './profilePic.model';
import { CreateProfilePicDto } from '../dto/profilePic.dto';
import { Id } from '../dto/id.dto';

@Injectable()
export class ProfilePicService {
  constructor(@InjectModel(ProfilePic.name) private readonly profilePicModel: Model<ProfilePic>) {}

  async createProfilePic(id :Id, buf: Buffer): Promise<ProfilePic> {
    console.log(id);
    const profilePic =  new this.profilePicModel({ _id: id, icon: buf });
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

  // Implement other CRUD operations as needed
}