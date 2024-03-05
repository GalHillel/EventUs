import { Controller, Post, Body, Get, UseInterceptors, UploadedFile, ParseFilePipeBuilder, HttpStatus, UploadedFiles, Param, Response, Req } from '@nestjs/common';
import { ProfilePicService } from './profilePic.service';
import { Response as Res } from 'express';
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

  // @Get(":id")
  // async getProfilePic(@Param('id') _id: string): Promise<String> {
  //   console.log("getting profile pic")
  //   const html = "<img src='data:image/png;base64," + (await this.profilePicService.getDecodedIcon(_id)) + "'/>"
  //   return html;
  // }
  
  @Get(':id')
  async getProfilePic(@Param('id') id: string, @Response() res: Res) {
    // Retrieve the image data (base64 encoded) from your profilePicService
    const imageData = await this.profilePicService.getDecodedIcon(id);
    console.log
    // Set the appropriate content type header for the response
    res.header('Content-Type', 'image/png');

    // Send the image data as the response body
    res.send(Buffer.from(imageData, 'base64'));
  }
  // Implement other CRUD endpoints as needed
}