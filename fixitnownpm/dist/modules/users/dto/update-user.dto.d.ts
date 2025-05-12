import { ServiceType } from '../../auth/enums/service-type.enum';
export declare class UpdateUserDto {
    name?: string;
    email?: string;
    phone?: string;
    gender?: string;
    cbeAccount?: string;
    paypalAccount?: string;
    telebirrAccount?: string;
    awashAccount?: string;
    serviceType?: ServiceType;
    password?: string;
    role?: string;
}
