import { Role } from '../enums/roles.enum';
import { ServiceType } from '../enums/service-type.enum';
import { Gender } from '../enums/gender.enum';
export declare class RegisterDto {
    name: string;
    phone: string;
    email: string;
    gender: Gender;
    role: Role;
    password: string;
    serviceType?: ServiceType;
    bankName?: string;
    accountNumber?: string;
    accountName?: string;
    cbeAccount?: string;
    paypalAccount?: string;
    telebirrAccount?: string;
    awashAccount?: string;
}
