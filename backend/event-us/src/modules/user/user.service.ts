import { Injectable } from '@nestjs/common';
import { InjectModel } from '@nestjs/mongoose';
import { Model } from 'mongoose';
import { User } from './user.model';
import { CreateUserDto } from '../dto/user.dto';

@Injectable()
export class UserService {
  constructor(@InjectModel(User.name) private readonly userModel: Model<User>) {}

  async createUser(createUserDto: CreateUserDto): Promise<User> {
    console.log("creating user" + createUserDto);
    const createdUser = new this.userModel(createUserDto);
    return createdUser.save();
  }

  async findAllUsers(): Promise<User[]> {
    return this.userModel.find().exec();
  }
  async printAllUsers(): Promise<void>{
    const users: User[] = await this.userModel.find().exec();
    
    users.forEach( (user,index)=>{
      console.log(user);
    });
  }

  // Implement other CRUD operations as needed
}