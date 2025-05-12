import { User } from '../../users/entities/user.entity';
import { Service } from '../../services/entities/service.entity';
import { ServiceType } from '../../auth/enums/service-type.enum';
export declare enum RequestStatus {
    PENDING = "PENDING",
    ACCEPTED = "ACCEPTED",
    REJECTED = "REJECTED",
    COMPLETED = "COMPLETED",
    CANCELLED = "CANCELLED",
    IN_PROGRESS = "IN_PROGRESS"
}
export declare class Request {
    request_id: number;
    serviceType: ServiceType;
    description: string;
    urgency: string;
    budget: number | null;
    status: RequestStatus;
    scheduledDate: Date | null;
    completionDate: Date | null;
    rating: number | null;
    review: string | null;
    requester: User;
    requester_id: number;
    provider: User | null;
    provider_id: number | null;
    service: Service | null;
    service_id: number | null;
    createdAt: Date;
    updatedAt: Date;
}
