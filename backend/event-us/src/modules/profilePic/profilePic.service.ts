import { Injectable } from '@nestjs/common';
import { InjectModel } from '@nestjs/mongoose';
import { Model } from 'mongoose';
import { ProfilePic } from './profilepic.model';
import { CreateProfilePicDto } from '../dto/profilepic.dto';

@Injectable()
export class ProfilePicService {
  constructor(@InjectModel(ProfilePic.name) private readonly profilepicModel: Model<ProfilePic>) {}

  async createProfilePic(createProfilePicDto: CreateProfilePicDto): Promise<ProfilePic> {
    console.log("creating profilepic" + createProfilePicDto);
    const createdProfilePic = new this.profilepicModel(createProfilePicDto);
    return createdProfilePic.save();
  }

  async findAllProfilePics(): Promise<ProfilePic[]> {
    return this.profilepicModel.find().exec();
  }
  async printAllProfilePics(): Promise<void>{
    const profilepics: ProfilePic[] = await this.profilepicModel.find().exec();
    
    profilepics.forEach( (profilepic,index)=>{
      console.log(profilepic);
    });
  }

  // Implement other CRUD operations as needed
}