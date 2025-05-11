import { IsEmail, IsString, IsNumber, IsOptional, MinLength, IsEnum } from 'class-validator';
import { ServiceType } from '../../auth/enums/service-type.enum';

export class UpdateUserDto {
  @IsString()
  @IsOptional()
  name?: string;

  @IsEmail()
  @IsOptional()
  email?: string;

  @IsString()
  @IsOptional()
  phone?: string;

  @IsString()
  @IsOptional()
  gender?: string;

  // Payment accounts - optional for all users
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

  // Provider specific fields
  @IsEnum(ServiceType)
  @IsOptional()
  serviceType?: ServiceType;

  @IsString()
  @MinLength(6)
  @IsOptional()
  password?: string;

  @IsString()
  @IsOptional()
  role?: string;
} 