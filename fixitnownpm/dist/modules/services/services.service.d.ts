import { Repository } from 'typeorm';
import { Service } from './entities/service.entity';
import { User } from '../users/entities/user.entity';
import { ServiceType } from '../auth/enums/service-type.enum';
interface CreateServiceDto {
    serviceType: ServiceType;
    images?: string[];
}
export declare class ServicesService {
    private serviceRepository;
    private userRepository;
    constructor(serviceRepository: Repository<Service>, userRepository: Repository<User>);
    create(createServiceDto: CreateServiceDto, provider_id: number): Promise<Service>;
    findAll(): Promise<Service[]>;
    findOne(id: number): Promise<Service>;
    findByProvider(provider_id: number): Promise<Service[]>;
    update(id: number, updateServiceDto: Partial<Service>): Promise<Service>;
    remove(id: number): Promise<void>;
    updateRating(id: number, rating: number): Promise<Service>;
}
export {};
