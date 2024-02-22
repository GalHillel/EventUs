import { Controller, Post, Body, Get, UseInterceptors, UploadedFile, ParseFilePipeBuilder, HttpStatus, UploadedFiles } from '@nestjs/common';
import { ProfilePicService } from './profilePic.service';
import { CreateProfilePicDto } from '../dto/profilePic.dto';
import { ProfilePic } from './profilePic.model';
import { FileFieldsInterceptor, FileInterceptor } from '@nestjs/platform-express';
//import { string } from '../dto/id.dto';
import { Multer } from 'multer';

@Controller('profilepics')
export class ProfilePicController {
  constructor(private readonly profilePicService: ProfilePicService) {}

  @Get()
  async findAllProfilePics(): Promise<ProfilePic[]> {
    this.profilePicService.printAllProfilePics();
    return this.profilePicService.findAllProfilePics();
  }

  @Post()
  @UseInterceptors(FileFieldsInterceptor([
    { name: 'icon', maxCount: 1 },
  ]))
  async uploadProfilePicture(@UploadedFiles() files: { _id?: Multer.File, icon?: Multer.File[] }) : Promise<String>{
    console.log(files)
    var params : {'icon':Buffer} = {'icon':files.icon[0].buffer} 
    return (await this.profilePicService.createProfilePic(params)).id;   
  }

  // Implement other CRUD endpoints as needed
}