import { IsEmail, IsString, IsEnum, IsOptional, MinLength } from 'class-validator';
import { Role } from '../../auth/enums/roles.enum';
import { ServiceType } from '../../auth/enums/service-type.enum';

export class CreateUserDto {
  @IsString()
  name: string;

  @IsEmail()
  email: string;

  @IsString()
  phone: string;

  @IsString()
  @MinLength(6)
  password: string;

  @IsEnum(Role)
  role: Role;

  @IsString()
  gender: string;

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

  // Provider specific field
  @IsEnum(ServiceType)
  @IsOptional()
  serviceType?: ServiceType;
} 