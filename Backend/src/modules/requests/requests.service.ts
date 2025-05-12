import { Injectable, NotFoundException, BadRequestException } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Repository } from 'typeorm';
import { Request, RequestStatus } from './entities/request.entity';
import { CreateRequestDto } from './dto/create-request.dto';
import { UpdateRequestDto } from './dto/update-request.dto';
import { User } from '../users/entities/user.entity';
import { Role } from '../auth/enums/roles.enum';

@Injectable()
export class RequestsService {
  constructor(
    @InjectRepository(Request)
    private requestRepository: Repository<Request>,
    @InjectRepository(User)
    private userRepository: Repository<User>,
  ) {}

  async create(createRequestDto: CreateRequestDto, requesterId: number): Promise<Request> {
    const requester = await this.userRepository.findOne({ where: { id: requesterId } });
    if (!requester) {
      throw new NotFoundException('Requester not found');
    }
    if (requester.role !== Role.REQUESTER) {
      throw new BadRequestException('Only requesters can create requests');
    }

    const request = this.requestRepository.create({
      ...createRequestDto,
      requester_id: requesterId,
      status: RequestStatus.PENDING
    });

    return this.requestRepository.save(request);
  }

  async findAll(): Promise<Request[]> {
    return this.requestRepository.find();
  }

  async findOne(request_id: number): Promise<Request> {
    const request = await this.requestRepository.findOne({ where: { request_id } });
    if (!request) {
      throw new NotFoundException(`Request with ID ${request_id} not found`);
    }
    return request;
  }

  async findByRequester(requesterId: number): Promise<Request[]> {
    const requester = await this.userRepository.findOne({ where: { id: requesterId } });
    if (!requester) {
      throw new NotFoundException('Requester not found');
    }
    if (requester.role !== Role.REQUESTER) {
      throw new BadRequestException('User is not a requester');
    }

    return this.requestRepository.find({ where: { requester_id: requesterId } });
  }

  async findByProvider(providerId: number): Promise<Request[]> {
    const provider = await this.userRepository.findOne({ where: { id: providerId } });
    if (!provider) {
      throw new NotFoundException('Provider not found');
    }
    if (provider.role !== Role.PROVIDER) {
      throw new BadRequestException('User is not a provider');
    }

    return this.requestRepository.find({ where: { provider_id: providerId } });
  }

   async acceptRequest(request_id: number, providerId: number): Promise<Request> {
    const request = await this.findOne(request_id);
    if (request.status !== RequestStatus.PENDING) {
      throw new BadRequestException('Request is not in pending status');
    }

    const provider = await this.userRepository.findOne({ where: { id: providerId } });
    if (!provider) {
      throw new NotFoundException('Provider not found');
    }
    if (provider.role !== Role.PROVIDER) {
      throw new BadRequestException('User is not a provider');
    }

    request.provider_id = providerId;
    request.status = RequestStatus.COMPLETED;
    return this.requestRepository.save(request);
  }


  async updateStatus(request_id: number, status: RequestStatus, userId: number): Promise<Request> {
    const request = await this.findOne(request_id);
    const user = await this.userRepository.findOne({ where: { id: userId } });

    if (!user) {
      throw new NotFoundException('User not found');
    }

    // Only requester, provider, or admin can update status
    if (user.role !== Role.ADMIN && 
        user.id !== request.requester_id && 
        user.id !== request.provider_id) {
      throw new BadRequestException('Not authorized to update this request');
    }

    request.status = status;
    if (status === RequestStatus.COMPLETED) {
      request.completionDate = new Date();
    }

    return this.requestRepository.save(request);
  }

  async update(request_id: number, updateRequestDto: UpdateRequestDto, userId: number): Promise<Request> {
    const request = await this.findOne(request_id);
    const user = await this.userRepository.findOne({ where: { id: userId } });

    if (!user) {
      throw new NotFoundException('User not found');
    }

    // Only requester or admin can update request details
    if (user.role !== Role.ADMIN && user.id !== request.requester_id) {
      throw new BadRequestException('Not authorized to update this request');
    }

    Object.assign(request, updateRequestDto);
    return this.requestRepository.save(request);
  }

  async remove(request_id: number, userId: number): Promise<void> {
    const request = await this.findOne(request_id);
    const user = await this.userRepository.findOne({ where: { id: userId } });

    if (!user) {
      throw new NotFoundException('User not found');
    }

    // Only requester or admin can delete request
    if (user.role !== Role.ADMIN && user.id !== request.requester_id) {
      throw new BadRequestException('Not authorized to delete this request');
    }

    await this.requestRepository.remove(request);
  }

  async addReview(request_id: number, rating: number, review: string, userId: number): Promise<Request> {
    const request = await this.findOne(request_id);
    const user = await this.userRepository.findOne({ where: { id: userId } });

    if (!user) {
      throw new NotFoundException('User not found');
    }

    // Only requester can add review
    if (user.id !== request.requester_id) {
      throw new BadRequestException('Only requester can add review');
    }

    if (request.status !== RequestStatus.COMPLETED) {
      throw new BadRequestException('Can only review completed requests');
    }

    request.rating = rating;
    request.review = review;
    return this.requestRepository.save(request);
  }
  async getCompletedRequests(): Promise<Request[]> {
    return this.requestRepository.find({
      where: { status: RequestStatus.COMPLETED },
      relations: ['requester', 'provider'],
      order: { createdAt: 'DESC' }
    });
  }

  async getRejectedRequests(): Promise<Request[]> {
    return this.requestRepository.find({
      where: { status: RequestStatus.REJECTED },
      relations: ['requester', 'provider'],
      order: { createdAt: 'DESC' }
    });
  }
   async findUnassignedRequests(): Promise<Request[]> {
  return this.requestRepository
    .createQueryBuilder('request')
    .leftJoinAndSelect('request.requester', 'requester')
    .where('request.provider_id IS NULL')
    .andWhere('request.status = :status', { status: RequestStatus.PENDING })
    .orderBy('request.createdAt', 'DESC')
    .getMany();
}

  async getProviderCompletedRequests(providerId: number): Promise<Request[]> {
    const provider = await this.userRepository.findOne({ where: { id: providerId } });
    if (!provider) {
      throw new NotFoundException('Provider not found');
    }
    if (provider.role !== Role.PROVIDER) {
      throw new BadRequestException('User is not a provider');
    }

    return this.requestRepository.find({
      where: { 
        provider_id: providerId,
        status: RequestStatus.COMPLETED 
      },
      relations: ['requester'],
      order: { completionDate: 'DESC' }
    });
  }

  async getProviderAcceptedRequests(providerId: number): Promise<Request[]> {
    const provider = await this.userRepository.findOne({ where: { id: providerId } });
    if (!provider) {
      throw new NotFoundException('Provider not found');
    }
    if (provider.role !== Role.PROVIDER) {
      throw new BadRequestException('User is not a provider');
    }

    return this.requestRepository.find({
      where: { 
        provider_id: providerId,
        status: RequestStatus.ACCEPTED 
      },
      relations: ['requester'],
      order: { createdAt: 'DESC' }
    });
  }

  async getProviderCompletedRequestCount(providerId: number): Promise<number> {
    const provider = await this.userRepository.findOne({ where: { id: providerId } });
    if (!provider) {
      throw new NotFoundException('Provider not found');
    }
    if (provider.role !== Role.PROVIDER) {
      throw new BadRequestException('User is not a provider');
    }

    return this.requestRepository.count({
      where: {
        provider_id: providerId,
        status: RequestStatus.COMPLETED
      }
    });
  }

  async getProviderAverageRating(providerId: number): Promise<number> {
    const provider = await this.userRepository.findOne({ where: { id: providerId } });
    if (!provider) {
      throw new NotFoundException('Provider not found');
    }
    if (provider.role !== Role.PROVIDER) {
      throw new BadRequestException('User is not a provider');
    }

    const result = await this.requestRepository
      .createQueryBuilder('request')
      .select('AVG(request.rating)', 'averageRating')
      .where('request.provider_id = :providerId', { providerId })
      .andWhere('request.status = :status', { status: RequestStatus.COMPLETED })
      .andWhere('request.rating IS NOT NULL')
      .getRawOne();

    return result?.averageRating || 0;
  }

  async getProviderTotalBudget(providerId: number): Promise<number> {
    const provider = await this.userRepository.findOne({ where: { id: providerId } });
    if (!provider) {
      throw new NotFoundException('Provider not found');
    }
    if (provider.role !== Role.PROVIDER) {
      throw new BadRequestException('User is not a provider');
    }

    const result = await this.requestRepository
      .createQueryBuilder('request')
      .select('SUM(request.budget)', 'totalBudget')
      .where('request.provider_id = :providerId', { providerId })
      .andWhere('request.status = :status', { status: RequestStatus.COMPLETED })
      .andWhere('request.budget IS NOT NULL')
      .getRawOne();

    return result?.totalBudget || 0;
  }

} 