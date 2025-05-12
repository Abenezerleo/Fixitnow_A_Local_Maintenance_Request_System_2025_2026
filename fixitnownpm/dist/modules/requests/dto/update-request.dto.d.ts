import { ServiceType } from '../../auth/enums/service-type.enum';
export declare class UpdateRequestDto {
    serviceType?: ServiceType;
    description?: string;
    urgency?: string;
    budget?: number;
    scheduledDate?: Date;
}
