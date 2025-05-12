import { Role } from '../../auth/enums/roles.enum';
import { ServiceType } from '../../auth/enums/service-type.enum';
export declare class CreateUserDto {
    name: string;
    email: string;
    phone: string;
    password: string;
    role: Role;
    gender: string;
    cbeAccount?: string;
    paypalAccount?: string;
    telebirrAccount?: string;
    awashAccount?: string;
    serviceType?: ServiceType;
}
