import { Module } from '@nestjs/common';
import { MongooseModule } from '@nestjs/mongoose';
import { ProfilePicController } from './profilepic.controller';
import { ProfilePicService } from './profilepic.service';
import { ProfilePic, ProfilePicSchema } from './profilepic.model';

@Module({
    imports: [MongooseModule.forFeature([{ name: ProfilePic.name, schema: ProfilePicSchema }])],
    controllers: [ProfilePicController],
    providers: [ProfilePicService],
  })
export class ProfilePicModule {}
