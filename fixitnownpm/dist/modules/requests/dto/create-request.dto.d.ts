import { ServiceType } from '../../auth/enums/service-type.enum';
export declare class CreateRequestDto {
    serviceType: ServiceType;
    description: string;
    urgency: string;
    budget: number;
    scheduledDate?: Date;
    service_id?: number;
}
