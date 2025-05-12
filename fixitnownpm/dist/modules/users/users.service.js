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
exports.UsersService = void 0;
const common_1 = require("@nestjs/common");
const typeorm_1 = require("@nestjs/typeorm");
const typeorm_2 = require("typeorm");
const user_entity_1 = require("./entities/user.entity");
const request_entity_1 = require("../requests/entities/request.entity");
const service_entity_1 = require("../services/entities/service.entity");
const roles_enum_1 = require("../auth/enums/roles.enum");
const service_type_enum_1 = require("../auth/enums/service-type.enum");
const bcrypt = require("bcrypt");
const typeorm_3 = require("typeorm");
let UsersService = class UsersService {
    userRepository;
    requestRepository;
    serviceRepository;
    constructor(userRepository, requestRepository, serviceRepository) {
        this.userRepository = userRepository;
        this.requestRepository = requestRepository;
        this.serviceRepository = serviceRepository;
    }
    async create(createUserDto) {
        const existingUser = await this.findByEmail(createUserDto.email);
        if (existingUser) {
            throw new common_1.BadRequestException('Email already exists');
        }
        if (!Object.values(roles_enum_1.Role).includes(createUserDto.role)) {
            throw new common_1.BadRequestException('Invalid role');
        }
        if (createUserDto.role === roles_enum_1.Role.PROVIDER && !createUserDto.serviceType) {
            throw new common_1.BadRequestException('Service type is required for providers');
        }
        const hashedPassword = await bcrypt.hash(createUserDto.password, 10);
        const user = this.userRepository.create({
            ...createUserDto,
            password: hashedPassword,
        });
        return this.userRepository.save(user);
    }
    async findAll() {
        return this.userRepository.find();
    }
    async findOne(id) {
        const user = await this.userRepository.findOne({ where: { id } });
        if (!user) {
            throw new common_1.NotFoundException(`User with ID ${id} not found`);
        }
        return user;
    }
    async findByEmail(email) {
        return this.userRepository.findOne({ where: { email } });
    }
    async update(id, updateUserDto, currentUser) {
        const user = await this.findOne(id);
        if (currentUser.role !== roles_enum_1.Role.ADMIN && currentUser.id !== id) {
            throw new common_1.ForbiddenException('You can only update your own profile');
        }
        if (updateUserDto.role && currentUser.role !== roles_enum_1.Role.ADMIN) {
            throw new common_1.BadRequestException('Only admin can change roles');
        }
        if (updateUserDto.serviceType && !Object.values(service_type_enum_1.ServiceType).includes(updateUserDto.serviceType)) {
            throw new common_1.BadRequestException('Invalid service type');
        }
        if (updateUserDto.password) {
            updateUserDto.password = await bcrypt.hash(updateUserDto.password, 10);
        }
        if (currentUser.role !== roles_enum_1.Role.ADMIN) {
            delete updateUserDto.role;
            if (currentUser.role !== roles_enum_1.Role.PROVIDER) {
                delete updateUserDto.serviceType;
            }
        }
        Object.assign(user, updateUserDto);
        return this.userRepository.save(user);
    }
    async remove(id, currentUser) {
        const user = await this.findOne(id);
        if (currentUser.role !== roles_enum_1.Role.ADMIN) {
            throw new common_1.BadRequestException('Only admin can delete users');
        }
        await this.userRepository.remove(user);
    }
    async getRequesters() {
        return this.userRepository.find({
            where: { role: roles_enum_1.Role.REQUESTER },
            select: ['id', 'name', 'email', 'phone', 'gender', 'cbeAccount', 'paypalAccount', 'telebirrAccount', 'awashAccount'],
        });
    }
    async getProviders() {
        return this.userRepository.find({
            where: { role: roles_enum_1.Role.PROVIDER },
            select: ['id', 'name', 'email', 'phone', 'gender', 'serviceType', 'providerRating', 'totalJobsCompleted', 'totalIncome'],
        });
    }
    async getTotalRequesters() {
        return this.userRepository.count({ where: { role: roles_enum_1.Role.REQUESTER } });
    }
    async getTotalProviders() {
        return this.userRepository.count({ where: { role: roles_enum_1.Role.PROVIDER } });
    }
    async findRequesterByName(name) {
        return this.userRepository.find({
            where: [
                { role: roles_enum_1.Role.REQUESTER, name: (0, typeorm_3.Like)(`%${name}%`) },
                { role: roles_enum_1.Role.REQUESTER, email: (0, typeorm_3.Like)(`%${name}%`) }
            ],
            select: ['id', 'name', 'email', 'phone', 'gender', 'cbeAccount', 'paypalAccount', 'telebirrAccount', 'awashAccount'],
        });
    }
    async findProviderByName(name) {
        return this.userRepository.find({
            where: [
                { role: roles_enum_1.Role.PROVIDER, name: (0, typeorm_3.Like)(`%${name}%`) },
                { role: roles_enum_1.Role.PROVIDER, email: (0, typeorm_3.Like)(`%${name}%`) }
            ],
            select: ['id', 'name', 'email', 'phone', 'gender', 'serviceType', 'providerRating', 'totalJobsCompleted', 'totalIncome'],
        });
    }
    async getRequestStatistics() {
        const [totalCompleted, totalRejected, totalPending] = await Promise.all([
            this.requestRepository.count({ where: { status: request_entity_1.RequestStatus.COMPLETED } }),
            this.requestRepository.count({ where: { status: request_entity_1.RequestStatus.REJECTED } }),
            this.requestRepository.count({ where: { status: request_entity_1.RequestStatus.PENDING } })
        ]);
        return {
            totalCompleted,
            totalRejected,
            totalPending
        };
    }
    async getTotalCompleted() {
        return this.requestRepository.count({ where: { status: request_entity_1.RequestStatus.COMPLETED } });
    }
    async getTotalRejected() {
        return this.requestRepository.count({ where: { status: request_entity_1.RequestStatus.REJECTED } });
    }
    async getTotalPending() {
        return this.requestRepository.count({ where: { status: request_entity_1.RequestStatus.PENDING } });
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
    async getTopRatedProviders() {
        return this.userRepository.find({
            where: { role: roles_enum_1.Role.PROVIDER },
            order: { providerRating: 'DESC' },
            take: 3,
            select: ['id', 'name', 'email', 'phone', 'gender', 'serviceType', 'providerRating', 'totalJobsCompleted', 'totalIncome'],
        });
    }
    async getProviderServices(providerId) {
        const provider = await this.findOne(providerId);
        if (provider.role !== roles_enum_1.Role.PROVIDER) {
            throw new common_1.BadRequestException('User is not a provider');
        }
        return this.serviceRepository.find({ where: { provider_id: providerId } });
    }
    async getProviderRequests(providerId) {
        const provider = await this.findOne(providerId);
        if (provider.role !== roles_enum_1.Role.PROVIDER) {
            throw new common_1.BadRequestException('User is not a provider');
        }
        return this.requestRepository.find({ where: { provider_id: providerId } });
    }
    async createRequest(requesterId, createRequestDto) {
        const requester = await this.findOne(requesterId);
        if (requester.role !== roles_enum_1.Role.REQUESTER) {
            throw new common_1.BadRequestException('User is not a requester');
        }
        const request = new request_entity_1.Request();
        request.serviceType = createRequestDto.serviceType;
        request.description = createRequestDto.description;
        request.urgency = createRequestDto.urgency;
        request.requester_id = requesterId;
        request.status = request_entity_1.RequestStatus.PENDING;
        return this.requestRepository.save(request);
    }
    async getRequesterRequests(requesterId) {
        const requester = await this.findOne(requesterId);
        if (requester.role !== roles_enum_1.Role.REQUESTER) {
            throw new common_1.BadRequestException('User is not a requester');
        }
        return this.requestRepository.find({ where: { requester_id: requesterId } });
    }
    async getProfile(userId) {
        return this.findOne(userId);
    }
    async getProviderPerformance(providerId) {
        const provider = await this.findOne(providerId);
        if (provider.role !== roles_enum_1.Role.PROVIDER) {
            throw new common_1.BadRequestException('User is not a provider');
        }
        const services = await this.serviceRepository.find({ where: { provider_id: providerId } });
        const requests = await this.requestRepository.find({ where: { provider_id: providerId } });
        return {
            totalServices: services.length,
            totalRequests: requests.length,
            completedRequests: requests.filter(r => r.status === request_entity_1.RequestStatus.COMPLETED).length,
            averageRating: provider.providerRating,
            totalIncome: provider.totalIncome
        };
    }
    async getPaymentAccounts(userId) {
        const user = await this.getProfile(userId);
        return {
            cbeAccount: user.cbeAccount,
            paypalAccount: user.paypalAccount,
            telebirrAccount: user.telebirrAccount,
            awashAccount: user.awashAccount,
        };
    }
};
exports.UsersService = UsersService;
exports.UsersService = UsersService = __decorate([
    (0, common_1.Injectable)(),
    __param(0, (0, typeorm_1.InjectRepository)(user_entity_1.User)),
    __param(1, (0, typeorm_1.InjectRepository)(request_entity_1.Request)),
    __param(2, (0, typeorm_1.InjectRepository)(service_entity_1.Service)),
    __metadata("design:paramtypes", [typeorm_2.Repository,
        typeorm_2.Repository,
        typeorm_2.Repository])
], UsersService);
//# sourceMappingURL=users.service.js.map