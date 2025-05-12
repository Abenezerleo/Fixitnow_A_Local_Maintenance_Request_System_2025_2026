import { Repository } from 'typeorm';
import { Request, RequestStatus } from './entities/request.entity';
import { CreateRequestDto } from './dto/create-request.dto';
import { UpdateRequestDto } from './dto/update-request.dto';
import { User } from '../users/entities/user.entity';
export declare class RequestsService {
    private requestRepository;
    private userRepository;
    constructor(requestRepository: Repository<Request>, userRepository: Repository<User>);
    create(createRequestDto: CreateRequestDto, requesterId: number): Promise<Request>;
    findAll(): Promise<Request[]>;
    findOne(request_id: number): Promise<Request>;
    findByRequester(requesterId: number): Promise<Request[]>;
    findByProvider(providerId: number): Promise<Request[]>;
    acceptRequest(request_id: number, providerId: number): Promise<Request>;
    updateStatus(request_id: number, status: RequestStatus, userId: number): Promise<Request>;
    update(request_id: number, updateRequestDto: UpdateRequestDto, userId: number): Promise<Request>;
    remove(request_id: number, userId: number): Promise<void>;
    addReview(request_id: number, rating: number, review: string, userId: number): Promise<Request>;
    getCompletedRequests(): Promise<Request[]>;
    getRejectedRequests(): Promise<Request[]>;
    findUnassignedRequests(): Promise<Request[]>;
    getProviderCompletedRequests(providerId: number): Promise<Request[]>;
    getProviderAcceptedRequests(providerId: number): Promise<Request[]>;
    getProviderCompletedRequestCount(providerId: number): Promise<number>;
    getProviderAverageRating(providerId: number): Promise<number>;
    getProviderTotalBudget(providerId: number): Promise<number>;
}
