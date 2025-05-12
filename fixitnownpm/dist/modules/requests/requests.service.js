"use strict";
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};
var __param = (this && this.__param) || function (paramIndex, decorator) {
    return function (target, key) { decorator(target, key, paramIndex); }
};
Object.defineProperty(exports, "__esModule", { value: true });
exports.RequestsService = void 0;
const common_1 = require("@nestjs/common");
const typeorm_1 = require("@nestjs/typeorm");
const typeorm_2 = require("typeorm");
const request_entity_1 = require("./entities/request.entity");
const user_entity_1 = require("../users/entities/user.entity");
const roles_enum_1 = require("../auth/enums/roles.enum");
let RequestsService = class RequestsService {
    requestRepository;
    userRepository;
    constructor(requestRepository, userRepository) {
        this.requestRepository = requestRepository;
        this.userRepository = userRepository;
    }
    async create(createRequestDto, requesterId) {
        const requester = await this.userRepository.findOne({ where: { id: requesterId } });
        if (!requester) {
            throw new common_1.NotFoundException('Requester not found');
        }
        if (requester.role !== roles_enum_1.Role.REQUESTER) {
            throw new common_1.BadRequestException('Only requesters can create requests');
        }
        const request = this.requestRepository.create({
            ...createRequestDto,
            requester_id: requesterId,
            status: request_entity_1.RequestStatus.PENDING
        });
        return this.requestRepository.save(request);
    }
    async findAll() {
        return this.requestRepository.find();
    }
    async findOne(request_id) {
        const request = await this.requestRepository.findOne({ where: { request_id } });
        if (!request) {
            throw new common_1.NotFoundException(`Request with ID ${request_id} not found`);
        }
        return request;
    }
    async findByRequester(requesterId) {
        const requester = await this.userRepository.findOne({ where: { id: requesterId } });
        if (!requester) {
            throw new common_1.NotFoundException('Requester not found');
        }
        if (requester.role !== roles_enum_1.Role.REQUESTER) {
            throw new common_1.BadRequestException('User is not a requester');
        }
        return this.requestRepository.find({ where: { requester_id: requesterId } });
    }
    async findByProvider(providerId) {
        const provider = await this.userRepository.findOne({ where: { id: providerId } });
        if (!provider) {
            throw new common_1.NotFoundException('Provider not found');
        }
        if (provider.role !== roles_enum_1.Role.PROVIDER) {
            throw new common_1.BadRequestException('User is not a provider');
        }
        return this.requestRepository.find({ where: { provider_id: providerId } });
    }
    async acceptRequest(request_id, providerId) {
        const request = await this.findOne(request_id);
        if (request.status !== request_entity_1.RequestStatus.PENDING) {
            throw new common_1.BadRequestException('Request is not in pending status');
        }
        const provider = await this.userRepository.findOne({ where: { id: providerId } });
        if (!provider) {
            throw new common_1.NotFoundException('Provider not found');
        }
        if (provider.role !== roles_enum_1.Role.PROVIDER) {
            throw new common_1.BadRequestException('User is not a provider');
        }
        request.provider_id = providerId;
        request.status = request_entity_1.RequestStatus.COMPLETED;
        return this.requestRepository.save(request);
    }
    async updateStatus(request_id, status, userId) {
        const request = await this.findOne(request_id);
        const user = await this.userRepository.findOne({ where: { id: userId } });
        if (!user) {
            throw new common_1.NotFoundException('User not found');
        }
        if (user.role !== roles_enum_1.Role.ADMIN &&
            user.id !== request.requester_id &&
            user.id !== request.provider_id) {
            throw new common_1.BadRequestException('Not authorized to update this request');
        }
        request.status = status;
        if (status === request_entity_1.RequestStatus.COMPLETED) {
            request.completionDate = new Date();
        }
        return this.requestRepository.save(request);
    }
    async update(request_id, updateRequestDto, userId) {
        const request = await this.findOne(request_id);
        const user = await this.userRepository.findOne({ where: { id: userId } });
        if (!user) {
            throw new common_1.NotFoundException('User not found');
        }
        if (user.role !== roles_enum_1.Role.ADMIN && user.id !== request.requester_id) {
            throw new common_1.BadRequestException('Not authorized to update this request');
        }
        Object.assign(request, updateRequestDto);
        return this.requestRepository.save(request);
    }
    async remove(request_id, userId) {
        const request = await this.findOne(request_id);
        const user = await this.userRepository.findOne({ where: { id: userId } });
        if (!user) {
            throw new common_1.NotFoundException('User not found');
        }
        if (user.role !== roles_enum_1.Role.ADMIN && user.id !== request.requester_id) {
            throw new common_1.BadRequestException('Not authorized to delete this request');
        }
        await this.requestRepository.remove(request);
    }
    async addReview(request_id, rating, review, userId) {
        const request = await this.findOne(request_id);
        const user = await this.userRepository.findOne({ where: { id: userId } });
        if (!user) {
            throw new common_1.NotFoundException('User not found');
        }
        if (user.id !== request.requester_id) {
            throw new common_1.BadRequestException('Only requester can add review');
        }
        if (request.status !== request_entity_1.RequestStatus.COMPLETED) {
            throw new common_1.BadRequestException('Can only review completed requests');
        }
        request.rating = rating;
        request.review = review;
        return this.requestRepository.save(request);
    }
    async getCompletedRequests() {
        return this.requestRepository.find({
            where: { status: request_entity_1.RequestStatus.COMPLETED },
            relations: ['requester', 'provider'],
            order: { createdAt: 'DESC' }
        });
    }
    async getRejectedRequests() {
        return this.requestRepository.find({
            where: { status: request_entity_1.RequestStatus.REJECTED },
            relations: ['requester', 'provider'],
            order: { createdAt: 'DESC' }
        });
    }
    async findUnassignedRequests() {
        return this.requestRepository
            .createQueryBuilder('request')
            .leftJoinAndSelect('request.requester', 'requester')
            .where('request.provider_id IS NULL')
            .andWhere('request.status = :status', { status: request_entity_1.RequestStatus.PENDING })
            .orderBy('request.createdAt', 'DESC')
            .getMany();
    }
    async getProviderCompletedRequests(providerId) {
        const provider = await this.userRepository.findOne({ where: { id: providerId } });
        if (!provider) {
            throw new common_1.NotFoundException('Provider not found');
        }
        if (provider.role !== roles_enum_1.Role.PROVIDER) {
            throw new common_1.BadRequestException('User is not a provider');
        }
        return this.requestRepository.find({
            where: {
                provider_id: providerId,
                status: request_entity_1.RequestStatus.COMPLETED
            },
            relations: ['requester'],
            order: { completionDate: 'DESC' }
        });
    }
    async getProviderAcceptedRequests(providerId) {
        const provider = await this.userRepository.findOne({ where: { id: providerId } });
        if (!provider) {
            throw new common_1.NotFoundException('Provider not found');
        }
        if (provider.role !== roles_enum_1.Role.PROVIDER) {
            throw new common_1.BadRequestException('User is not a provider');
        }
        return this.requestRepository.find({
            where: {
                provider_id: providerId,
                status: request_entity_1.RequestStatus.ACCEPTED
            },
            relations: ['requester'],
            order: { createdAt: 'DESC' }
        });
    }
    async getProviderCompletedRequestCount(providerId) {
        const provider = await this.userRepository.findOne({ where: { id: providerId } });
        if (!provider) {
            throw new common_1.NotFoundException('Provider not found');
        }
        if (provider.role !== roles_enum_1.Role.PROVIDER) {
            throw new common_1.BadRequestException('User is not a provider');
        }
        return this.requestRepository.count({
            where: {
                provider_id: providerId,
                status: request_entity_1.RequestStatus.COMPLETED
            }
        });
    }
    async getProviderAverageRating(providerId) {
        const provider = await this.userRepository.findOne({ where: { id: providerId } });
        if (!provider) {
            throw new common_1.NotFoundException('Provider not found');
        }
        if (provider.role !== roles_enum_1.Role.PROVIDER) {
            throw new common_1.BadRequestException('User is not a provider');
        }
        const result = await this.requestRepository
            .createQueryBuilder('request')
            .select('AVG(request.rating)', 'averageRating')
            .where('request.provider_id = :providerId', { providerId })
            .andWhere('request.status = :status', { status: request_entity_1.RequestStatus.COMPLETED })
            .andWhere('request.rating IS NOT NULL')
            .getRawOne();
        return result?.averageRating || 0;
    }
    async getProviderTotalBudget(providerId) {
        const provider = await this.userRepository.findOne({ where: { id: providerId } });
        if (!provider) {
            throw new common_1.NotFoundException('Provider not found');
        }
        if (provider.role !== roles_enum_1.Role.PROVIDER) {
            throw new common_1.BadRequestException('User is not a provider');
        }
        const result = await this.requestRepository
            .createQueryBuilder('request')
            .select('SUM(request.budget)', 'totalBudget')
            .where('request.provider_id = :providerId', { providerId })
            .andWhere('request.status = :status', { status: request_entity_1.RequestStatus.COMPLETED })
            .andWhere('request.budget IS NOT NULL')
            .getRawOne();
        return result?.totalBudget || 0;
    }
};
exports.RequestsService = RequestsService;
exports.RequestsService = RequestsService = __decorate([
    (0, common_1.Injectable)(),
    __param(0, (0, typeorm_1.InjectRepository)(request_entity_1.Request)),
    __param(1, (0, typeorm_1.InjectRepository)(user_entity_1.User)),
    __metadata("design:paramtypes", [typeorm_2.Repository,
        typeorm_2.Repository])
], RequestsService);
//# sourceMappingURL=requests.service.js.map