import { IsString, IsEmail, IsNotEmpty, MinLength, Matches, IsEnum, IsOptional } from 'class-validator';
import { Role } from '../enums/roles.enum';
import { ServiceType } from '../enums/service-type.enum';
import { Gender } from '../enums/gender.enum';

export class RegisterDto {
  @IsString()
  @IsNotEmpty()
  name: string;

  @IsString()
  @IsNotEmpty()
  phone: string;

  @IsEmail()
  @IsNotEmpty()
  email: string;

  @IsEnum(Gender)
  @IsNotEmpty()
  gender: Gender;

  @IsEnum(Role)
  @IsNotEmpty()
  role: Role;

  @IsString()
  @IsNotEmpty()
  @MinLength(6)
  password: string;

  @IsEnum(ServiceType)
  @IsOptional()
  serviceType?: ServiceType;

  @IsString()
  @IsOptional()
  bankName?: string;

  @IsString()
  @IsOptional()
  accountNumber?: string;

  @IsString()
  @IsOptional()
  accountName?: string;

  // Payment accounts
  @IsString()
  @IsOptional()
  cbeAccount?: string;

  @IsString()
  @IsOptional()
  paypalAccount?: string;

  @IsString()
  @IsOptional()
  telebirrAccount?: string;

  @IsString()
  @IsOptional()
  awashAccount?: string; 
} 