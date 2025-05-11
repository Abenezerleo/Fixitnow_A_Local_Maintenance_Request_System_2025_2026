import { IsString, IsNotEmpty, IsNumber, Min, IsOptional, IsDateString, IsEnum } from 'class-validator';
import { ServiceType } from '../../auth/enums/service-type.enum';

export class CreateRequestDto {
  @IsEnum(ServiceType)
  @IsNotEmpty()
  serviceType: ServiceType;

  @IsString()
  @IsNotEmpty()
  description: string;

  @IsString()
  @IsNotEmpty()
  urgency: string;

  @IsNumber()
  @Min(0)
  budget: number;

  @IsDateString()
  @IsOptional()
  scheduledDate?: Date;

  @IsNumber()
  @IsOptional()
  service_id?: number;
} 