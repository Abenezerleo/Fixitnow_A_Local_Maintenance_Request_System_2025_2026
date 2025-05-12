import { Injectable, NotFoundException, BadRequestException, ForbiddenException } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Repository } from 'typeorm';
import { User } from './entities/user.entity';
import { Request, RequestStatus } from '../requests/entities/request.entity';
import { Service } from '../services/entities/service.entity';
import { CreateUserDto } from './dto/create-user.dto';
import { UpdateUserDto } from './dto/update-user.dto';
import { CreateRequestDto } from '../requests/dto/create-request.dto';
import { Role } from '../auth/enums/roles.enum';
import { ServiceType } from '../auth/enums/service-type.enum';
import * as bcrypt from 'bcrypt';
import { Like } from 'typeorm';

@Injectable()
export class UsersService {
  constructor(
    @InjectRepository(User)
    private userRepository: Repository<User>,
    @InjectRepository(Request)
    private requestRepository: Repository<Request>,
    @InjectRepository(Service)
    private serviceRepository: Repository<Service>,
  ) {}

  async create(createUserDto: CreateUserDto): Promise<User> {
    // Check if email already exists
    const existingUser = await this.findByEmail(createUserDto.email);
    if (existingUser) {
      throw new BadRequestException('Email already exists');
    }

    // Validate role
    if (!Object.values(Role).includes(createUserDto.role)) {
      throw new BadRequestException('Invalid role');
    }

    // Validate service type for providers
    if (createUserDto.role === Role.PROVIDER && !createUserDto.serviceType) {
      throw new BadRequestException('Service type is required for providers');
    }

    // Hash password
    const hashedPassword = await bcrypt.hash(createUserDto.password, 10);

    const user = this.userRepository.create({
      ...createUserDto,
      password: hashedPassword,
    });

    return this.userRepository.save(user);
  }

  async findAll(): Promise<User[]> {
    return this.userRepository.find();
  }

  async findOne(id: number): Promise<User> {
    const user = await this.userRepository.findOne({ where: { id } });
    if (!user) {
      throw new NotFoundException(`User with ID ${id} not found`);
    }
    return user;
  }

  async findByEmail(email: string): Promise<User | null> {
    return this.userRepository.findOne({ where: { email } });
  }

  async update(id: number, updateUserDto: UpdateUserDto, currentUser: User): Promise<User> {
    const user = await this.findOne(id);

    // Only admin or the user themselves can update
    if (currentUser.role !== Role.ADMIN && currentUser.id !== id) {
      throw new ForbiddenException('You can only update your own profile');
    }

    // Only admin can change roles
    if (updateUserDto.role && currentUser.role !== Role.ADMIN) {
      throw new BadRequestException('Only admin can change roles');
    }

    // Validate service type if provided
    if (updateUserDto.serviceType && !Object.values(ServiceType).includes(updateUserDto.serviceType)) {
      throw new BadRequestException('Invalid service type');
    }

    // Hash password if provided
    if (updateUserDto.password) {
      updateUserDto.password = await bcrypt.hash(updateUserDto.password, 10);
    }

    // Remove sensitive fields if not admin
    if (currentUser.role !== Role.ADMIN) {
      delete updateUserDto.role;
      // Allow providers to update their serviceType
      if (currentUser.role !== Role.PROVIDER) {
        delete updateUserDto.serviceType;
      }
    }

    Object.assign(user, updateUserDto);
    return this.userRepository.save(user);
  }

  async remove(id: number, currentUser: User): Promise<void> {
    const user = await this.findOne(id);

    if (currentUser.role !== Role.ADMIN) {
      throw new BadRequestException('Only admin can delete users');
    }

    await this.userRepository.remove(user);
  }

  // Admin specific methods
  async getRequesters(): Promise<User[]> {
    return this.userRepository.find({
      where: { role: Role.REQUESTER },
      select: ['id', 'name', 'email', 'phone', 'gender', 'cbeAccount', 'paypalAccount', 'telebirrAccount', 'awashAccount'],
    });
  }

  async getProviders(): Promise<User[]> {
    return this.userRepository.find({
      where: { role: Role.PROVIDER },
      select: ['id', 'name', 'email', 'phone', 'gender', 'serviceType', 'providerRating', 'totalJobsCompleted', 'totalIncome'],
    });
  }

  // New methods for admin
  async getTotalRequesters(): Promise<number> {
    return this.userRepository.count({ where: { role: Role.REQUESTER } });
  }

  async getTotalProviders(): Promise<number> {
    return this.userRepository.count({ where: { role: Role.PROVIDER } });
  }

  async findRequesterByName(name: string): Promise<User[]> {
    return this.userRepository.find({
      where: [
        { role: Role.REQUESTER, name: Like(`%${name}%`) },
        { role: Role.REQUESTER, email: Like(`%${name}%`) }
      ],
      select: ['id', 'name', 'email', 'phone', 'gender', 'cbeAccount', 'paypalAccount', 'telebirrAccount', 'awashAccount'],
    });
  }

  async findProviderByName(name: string): Promise<User[]> {
    return this.userRepository.find({
      where: [
        { role: Role.PROVIDER, name: Like(`%${name}%`) },
        { role: Role.PROVIDER, email: Like(`%${name}%`) }
      ],
      select: ['id', 'name', 'email', 'phone', 'gender', 'serviceType', 'providerRating', 'totalJobsCompleted', 'totalIncome'],
    });
  }

  async getRequestStatistics(): Promise<{
    totalCompleted: number;
    totalRejected: number;
    totalPending: number;
  }> {
    const [totalCompleted, totalRejected, totalPending] = await Promise.all([
      this.requestRepository.count({ where: { status: RequestStatus.COMPLETED } }),
      this.requestRepository.count({ where: { status: RequestStatus.REJECTED } }),
      this.requestRepository.count({ where: { status: RequestStatus.PENDING } })
    ]);

    return {
      totalCompleted,
      totalRejected,
      totalPending
    };
  }

  async getTotalCompleted(): Promise<number> {
    return this.requestRepository.count({ where: { status: RequestStatus.COMPLETED } });
  }

  async getTotalRejected(): Promise<number> {
    return this.requestRepository.count({ where: { status: RequestStatus.REJECTED } });
  }

  async getTotalPending(): Promise<number> {
    return this.requestRepository.count({ where: { status: RequestStatus.PENDING } });
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

  async getTopRatedProviders(): Promise<User[]> {
    return this.userRepository.find({
      where: { role: Role.PROVIDER },
      order: { providerRating: 'DESC' },
      take: 3,
      select: ['id', 'name', 'email', 'phone', 'gender', 'serviceType', 'providerRating', 'totalJobsCompleted', 'totalIncome'],
    });
  }

  // Provider specific methods
  async getProviderServices(providerId: number): Promise<Service[]> {
    const provider = await this.findOne(providerId);
    if (provider.role !== Role.PROVIDER) {
      throw new BadRequestException('User is not a provider');
    }
    return this.serviceRepository.find({ where: { provider_id: providerId } });
  }

  async getProviderRequests(providerId: number): Promise<Request[]> {
    const provider = await this.findOne(providerId);
    if (provider.role !== Role.PROVIDER) {
      throw new BadRequestException('User is not a provider');
    }
    return this.requestRepository.find({ where: { provider_id: providerId } });
  }

  // Requester specific methods
  async createRequest(requesterId: number, createRequestDto: CreateRequestDto): Promise<Request> {
    const requester = await this.findOne(requesterId);
    if (requester.role !== Role.REQUESTER) {
      throw new BadRequestException('User is not a requester');
    }

    const request = new Request();
    request.serviceType = createRequestDto.serviceType;
    request.description = createRequestDto.description;
    request.urgency = createRequestDto.urgency;
    request.requester_id = requesterId;
    request.status = RequestStatus.PENDING;

    return this.requestRepository.save(request);
  }

  async getRequesterRequests(requesterId: number): Promise<Request[]> {
    const requester = await this.findOne(requesterId);
    if (requester.role !== Role.REQUESTER) {
      throw new BadRequestException('User is not a requester');
    }
    return this.requestRepository.find({ where: { requester_id: requesterId } });
  }

  // Profile methods
  async getProfile(userId: number): Promise<User> {
    return this.findOne(userId);
  }

  async getProviderPerformance(providerId: number): Promise<any> {
    const provider = await this.findOne(providerId);
    if (provider.role !== Role.PROVIDER) {
      throw new BadRequestException('User is not a provider');
    }

    const services = await this.serviceRepository.find({ where: { provider_id: providerId } });
    const requests = await this.requestRepository.find({ where: { provider_id: providerId } });

    return {
      totalServices: services.length,
      totalRequests: requests.length,
      completedRequests: requests.filter(r => r.status === RequestStatus.COMPLETED).length,
      averageRating: provider.providerRating,
      totalIncome: provider.totalIncome
    };
  }

  async getPaymentAccounts(userId: number): Promise<Partial<User>> {
    const user = await this.getProfile(userId);
    return {
      cbeAccount: user.cbeAccount,
      paypalAccount: user.paypalAccount,
      telebirrAccount: user.telebirrAccount,
      awashAccount: user.awashAccount,
    };
  }
} 