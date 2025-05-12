import { UsersService } from './users.service';
import { CreateUserDto } from './dto/create-user.dto';
import { UpdateUserDto } from './dto/update-user.dto';
export declare class UsersController {
    private readonly usersService;
    constructor(usersService: UsersService);
    create(createUserDto: CreateUserDto): Promise<import("./entities/user.entity").User>;
    findAll(): Promise<import("./entities/user.entity").User[]>;
    getRequesters(): Promise<import("./entities/user.entity").User[]>;
    getProviders(): Promise<import("./entities/user.entity").User[]>;
    getTotalRequesters(): Promise<number>;
    getTotalProviders(): Promise<number>;
    findRequesterByName(name: string): Promise<import("./entities/user.entity").User[]>;
    findProviderByName(name: string): Promise<import("./entities/user.entity").User[]>;
    getRequestStatistics(): Promise<{
        totalCompleted: number;
        totalRejected: number;
        totalPending: number;
    }>;
    getTotalCompleted(): Promise<number>;
    getTotalRejected(): Promise<number>;
    getTotalPending(): Promise<number>;
    getCompletedRequests(): Promise<import("../requests/entities/request.entity").Request[]>;
    getRejectedRequests(): Promise<import("../requests/entities/request.entity").Request[]>;
    getProfile(req: any): Promise<import("./entities/user.entity").User>;
    getTopRatedProviders(): Promise<import("./entities/user.entity").User[]>;
    getProviderPerformance(req: any): Promise<any>;
    findOne(id: string): Promise<import("./entities/user.entity").User>;
    update(id: string, updateUserDto: UpdateUserDto, req: any): Promise<import("./entities/user.entity").User>;
    remove(id: string, req: any): Promise<void>;
}
