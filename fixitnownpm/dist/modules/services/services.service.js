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
exports.ServicesService = void 0;
const common_1 = require("@nestjs/common");
const typeorm_1 = require("@nestjs/typeorm");
const typeorm_2 = require("typeorm");
const service_entity_1 = require("./entities/service.entity");
const user_entity_1 = require("../users/entities/user.entity");
let ServicesService = class ServicesService {
    serviceRepository;
    userRepository;
    constructor(serviceRepository, userRepository) {
        this.serviceRepository = serviceRepository;
        this.userRepository = userRepository;
    }
    async create(createServiceDto, provider_id) {
        const provider = await this.userRepository.findOne({ where: { id: provider_id } });
        if (!provider) {
            throw new common_1.NotFoundException('Provider not found');
        }
        const service = new service_entity_1.Service();
        service.serviceType = createServiceDto.serviceType;
        service.provider_id = provider_id;
        service.images = createServiceDto.images ? JSON.stringify(createServiceDto.images) : '';
        service.rating = 0;
        return this.serviceRepository.save(service);
    }
    async findAll() {
        return this.serviceRepository.find({
            relations: ['provider'],
        });
    }
    async findOne(id) {
        const service = await this.serviceRepository.findOne({
            where: { id },
            relations: ['provider'],
        });
        if (!service) {
            throw new common_1.NotFoundException(`Service with ID ${id} not found`);
        }
        return service;
    }
    async findByProvider(provider_id) {
        return this.serviceRepository.find({
            where: { provider_id },
            relations: ['provider'],
        });
    }
    async update(id, updateServiceDto) {
        const service = await this.findOne(id);
        if (updateServiceDto.images) {
            updateServiceDto.images = JSON.stringify(updateServiceDto.images);
        }
        Object.assign(service, updateServiceDto);
        return this.serviceRepository.save(service);
    }
    async remove(id) {
        const result = await this.serviceRepository.delete(id);
        if (result.affected === 0) {
            throw new common_1.NotFoundException(`Service with ID ${id} not found`);
        }
    }
    async updateRating(id, rating) {
        const service = await this.findOne(id);
        service.rating = rating;
        const provider = await this.userRepository.findOne({ where: { id: service.provider_id } });
        if (provider) {
            const providerServices = await this.serviceRepository.find({
                where: { provider_id: provider.id },
            });
            const totalRating = providerServices.reduce((sum, s) => sum + s.rating, 0);
            provider.providerRating = totalRating / providerServices.length;
            await this.userRepository.save(provider);
        }
        return this.serviceRepository.save(service);
    }
};
exports.ServicesService = ServicesService;
exports.ServicesService = ServicesService = __decorate([
    (0, common_1.Injectable)(),
    __param(0, (0, typeorm_1.InjectRepository)(service_entity_1.Service)),
    __param(1, (0, typeorm_1.InjectRepository)(user_entity_1.User)),
    __metadata("design:paramtypes", [typeorm_2.Repository,
        typeorm_2.Repository])
], ServicesService);
//# sourceMappingURL=services.service.js.map