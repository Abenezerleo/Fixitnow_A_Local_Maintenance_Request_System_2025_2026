import { User } from '../../users/entities/user.entity';
import { ServiceType } from '../../auth/enums/service-type.enum';
export declare class Service {
    id: number;
    serviceType: ServiceType;
    rating: number;
    images: string;
    provider: User;
    provider_id: number;
}
