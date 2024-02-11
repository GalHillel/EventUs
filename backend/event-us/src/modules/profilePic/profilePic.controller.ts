import { Controller, Post, Body, Get, UseInterceptors, UploadedFile, ParseFilePipeBuilder, HttpStatus, UploadedFiles, BadRequestException, InternalServerErrorException } from '@nestjs/common';
import { ProfilePicService } from './profilePic.service';
import { CreateProfilePicDto } from '../dto/profilePic.dto';
import { ProfilePic } from './profilePic.model';
import { FileFieldsInterceptor, FileInterceptor } from '@nestjs/platform-express';
//import { string } from '../dto/id.dto';
import { Multer } from 'multer';

@Controller('profilepics')
export class ProfilePicController {
  constructor(private readonly profilePicService: ProfilePicService) {}

  @Post()
  @UseInterceptors(FileInterceptor("icon"))
  async uploadProfilePicture(@UploadedFiles() files: { icon?: Multer.File[] }): Promise<string> {
    try {
      if (!files || !files.icon || files.icon.length === 0) {
        throw new BadRequestException("No file uploaded");
      }
      const fileBuffer: Buffer = files.icon[0].buffer;
      return (await this.profilePicService.createProfilePic({ icon: fileBuffer })).id;;
    } catch (error) {
      console.error("Error uploading profile picture:", error);
      throw new InternalServerErrorException("Error uploading profile picture");
    }
  }


  @Get()
  async findAllProfilePics(): Promise<ProfilePic[]> {
    this.profilePicService.printAllProfilePics();
    return this.profilePicService.findAllProfilePics();
  }

  // Implement other CRUD endpoints as needed
}