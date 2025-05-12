import { RequestsService } from './requests.service';
import { CreateRequestDto } from './dto/create-request.dto';
import { UpdateRequestDto } from './dto/update-request.dto';
import { RequestStatus } from './entities/request.entity';
export declare class RequestsController {
    private readonly requestsService;
    constructor(requestsService: RequestsService);
    create(createRequestDto: CreateRequestDto, req: any): Promise<import("./entities/request.entity").Request>;
    getCompletedRequests(): Promise<import("./entities/request.entity").Request[]>;
    getRejectedRequests(): Promise<import("./entities/request.entity").Request[]>;
    findAll(): Promise<import("./entities/request.entity").Request[]>;
    findMyRequests(req: any): Promise<import("./entities/request.entity").Request[]>;
    findProviderRequests(req: any): Promise<import("./entities/request.entity").Request[]>;
    findProviderCompletedRequests(req: any): Promise<import("./entities/request.entity").Request[]>;
    findProviderAcceptedRequests(req: any): Promise<import("./entities/request.entity").Request[]>;
    getProviderCompletedCount(req: any): Promise<number>;
    getProviderAverageRating(req: any): Promise<number>;
    getProviderTotalBudget(req: any): Promise<number>;
    findUnassignedRequests(): Promise<import("./entities/request.entity").Request[]>;
    findOne(id: string): Promise<import("./entities/request.entity").Request>;
    acceptRequest(id: string, req: any): Promise<import("./entities/request.entity").Request>;
    updateStatus(id: string, status: RequestStatus, req: any): Promise<import("./entities/request.entity").Request>;
    update(id: string, updateRequestDto: UpdateRequestDto, req: any): Promise<import("./entities/request.entity").Request>;
    remove(id: string, req: any): Promise<void>;
    addReview(id: string, rating: number, review: string, req: any): Promise<import("./entities/request.entity").Request>;
}
