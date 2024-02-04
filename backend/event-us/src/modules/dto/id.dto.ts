import { Schema as mongooseSchema } from "mongoose";
import { ApiProperty } from '@nestjs/swagger';


export class Id{
    
    readonly _id: string
}

