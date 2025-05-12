import { Injectable, NotFoundException } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Repository } from 'typeorm';
import { Service } from './entities/service.entity';
import { User } from '../users/entities/user.entity';
import { ServiceType } from '../auth/enums/service-type.enum';

interface CreateServiceDto {
  serviceType: ServiceType;
  images?: string[];
}

@Injectable()
export class ServicesService {
  constructor(
    @InjectRepository(Service)
    private serviceRepository: Repository<Service>,
    @InjectRepository(User)
    private userRepository: Repository<User>,
  ) {}

  async create(createServiceDto: CreateServiceDto, provider_id: number): Promise<Service> {
    const provider = await this.userRepository.findOne({ where: { id: provider_id } });
    if (!provider) {
      throw new NotFoundException('Provider not found');
    }

    const service = new Service();
    service.serviceType = createServiceDto.serviceType;
    service.provider_id = provider_id;
    service.images = createServiceDto.images ? JSON.stringify(createServiceDto.images) : '';
    service.rating = 0;

    return this.serviceRepository.save(service);
  }

  async findAll(): Promise<Service[]> {
    return this.serviceRepository.find({
      relations: ['provider'],
    });
  }

  async findOne(id: number): Promise<Service> {
    const service = await this.serviceRepository.findOne({
      where: { id },
      relations: ['provider'],
    });

    if (!service) {
      throw new NotFoundException(`Service with ID ${id} not found`);
    }

    return service;
  }

  async findByProvider(provider_id: number): Promise<Service[]> {
    return this.serviceRepository.find({
      where: { provider_id },
      relations: ['provider'],
    });
  }

  async update(id: number, updateServiceDto: Partial<Service>): Promise<Service> {
    const service = await this.findOne(id);
    
    if (updateServiceDto.images) {
      updateServiceDto.images = JSON.stringify(updateServiceDto.images);
    }

    Object.assign(service, updateServiceDto);
    return this.serviceRepository.save(service);
  }

  async remove(id: number): Promise<void> {
    const result = await this.serviceRepository.delete(id);
    if (result.affected === 0) {
      throw new NotFoundException(`Service with ID ${id} not found`);
    }
  }

  async updateRating(id: number, rating: number): Promise<Service> {
    const service = await this.findOne(id);
    service.rating = rating;
    
    // Update provider's overall rating
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
} 