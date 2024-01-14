import { Controller, Post, Body, Get, UseInterceptors, UploadedFile, ParseFilePipeBuilder, HttpStatus, UploadedFiles } from '@nestjs/common';
import { ProfilePicService } from './profilePic.service';
import { CreateProfilePicDto } from '../dto/profilePic.dto';
import { ProfilePic } from './profilePic.model';
import { FileFieldsInterceptor, FileInterceptor } from '@nestjs/platform-express';
import { Id } from '../dto/id.dto';

@Controller('profilepics')
export class ProfilePicController {
  constructor(private readonly profilePicService: ProfilePicService) {}

  @Post()
  @UseInterceptors(FileFieldsInterceptor([
    { name: '_id', maxCount: 2 },
    { name: 'icon', maxCount: 2 },
  ]))
  async uploadProfilePicture(@UploadedFiles() files: { idData?: Express.Multer.File[], iconData?: Express.Multer.File[] }){//: Promise<ProfilePic> {
    console.log(files);
    
    //const icon = file.buffer;
    //console.log(icon.toString());
    //return this.profilePicService.createProfilePic(id, icon);
   
  }


  @Get()
  async findAllProfilePics(): Promise<ProfilePic[]> {
    this.profilePicService.printAllProfilePics();
    return this.profilePicService.findAllProfilePics();
  }

  // Implement other CRUD endpoints as needed
}