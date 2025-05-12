import { IsEnum, IsString, IsNumber, IsOptional, IsDate } from 'class-validator';
import { ServiceType } from '../../auth/enums/service-type.enum';

export class UpdateRequestDto {
  @IsEnum(ServiceType)
  @IsOptional()
  serviceType?: ServiceType;

  @IsString()
  @IsOptional()
  description?: string;

  @IsString()
  @IsOptional()
  urgency?: string;

  @IsNumber()
  @IsOptional()
  budget?: number;

  @IsDate()
  @IsOptional()
  scheduledDate?: Date;
} 