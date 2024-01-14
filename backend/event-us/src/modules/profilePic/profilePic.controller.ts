import { Controller, Post, Body, Get } from '@nestjs/common';
import { ProfilePicService } from './profilepic.service';
import { CreateProfilePicDto } from '../dto/profilepic.dto';
import { ProfilePic } from './profilepic.model';

@Controller('profilepics')
export class ProfilePicController {
  constructor(private readonly profilepicService: ProfilePicService) {}

  @Post()
  async createProfilePic(@Body() createProfilePicDto: CreateProfilePicDto): Promise<ProfilePic> {
    return this.profilepicService.createProfilePic(createProfilePicDto);
  }

  @Get()
  async findAllProfilePics(): Promise<ProfilePic[]> {
    this.profilepicService.printAllProfilePics();
    return this.profilepicService.findAllProfilePics();
  }

  // Implement other CRUD endpoints as needed
}