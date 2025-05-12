import { Role } from '../../auth/enums/roles.enum';
import { ServiceType } from '../../auth/enums/service-type.enum';
export declare class User {
    id: number;
    name: string;
    email: string;
    phone: string;
    password: string;
    role: Role;
    gender: string;
    serviceType?: ServiceType;
    cbeAccount?: string;
    paypalAccount?: string;
    telebirrAccount?: string;
    awashAccount?: string;
    age?: number;
    height?: number;
    weight?: number;
    joinDate?: string;
    membershipStatus?: string;
    providerRating?: number;
    totalJobsCompleted?: number;
    totalIncome?: number;
}
