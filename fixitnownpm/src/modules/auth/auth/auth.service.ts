import { Injectable, UnauthorizedException, BadRequestException } from '@nestjs/common';
import { JwtService } from '@nestjs/jwt';
import { InjectRepository } from '@nestjs/typeorm';
import { Repository } from 'typeorm';
import { User } from '../users/entities/user.entity';
import { RegisterDto } from './dto/register.dto';
import { LoginDto } from './dto/login.dto';
import { Role } from './enums/roles.enum';
import * as bcrypt from 'bcrypt';
import { TokenBlacklistService } from './token-blacklist.service';

@Injectable()
export class AuthService {
  constructor(
    @InjectRepository(User)
    private userRepository: Repository<User>,
    private jwtService: JwtService,
    private tokenBlacklistService: TokenBlacklistService,
  ) {}

  async register(registerDto: RegisterDto): Promise<User> {
    const { email, password, role, serviceType, name, phone, gender } = registerDto;

    // Check if user already exists
    const existingUser = await this.userRepository.findOne({ where: { email } });
    if (existingUser) {
      throw new BadRequestException('Email already registered');
    }

    // Check if username is taken
    const existingUsername = await this.userRepository.findOne({ where: { name } });
    if (existingUsername) {
      throw new BadRequestException('Username already taken');
    }

    // Validate service type for providers
    if (role === Role.PROVIDER && !serviceType) {
      throw new BadRequestException('Service type is required for providers');
    }

    // Hash password
    const hashedPassword = await bcrypt.hash(password, 10);

    // Create user based on role
    const user = new User();
    Object.assign(user, {
      ...registerDto,
      password: hashedPassword,
      serviceType: role === Role.PROVIDER ? serviceType : null,
      providerRating: role === Role.PROVIDER ? 0 : null,
      totalJobsCompleted: role === Role.PROVIDER ? 0 : null,
      totalIncome: role === Role.PROVIDER ? 0 : null,
    });

    return this.userRepository.save(user);
  }

  async login(loginDto: LoginDto): Promise<{ access_token: string }> {
    const { name, password } = loginDto;
    const user = await this.userRepository.findOne({ where: { name: name } });

    if (!user) {
      throw new UnauthorizedException('Invalid credentials');
    }

    const isPasswordValid = await bcrypt.compare(password, user.password);
    if (!isPasswordValid) {
      throw new UnauthorizedException('Invalid credentials');
    }

    if (!user.id) {
      throw new UnauthorizedException('Invalid user data');
    }

    const payload = { 
      sub: user.id,
      name: user.name,
      role: user.role 
    };

    return {
      access_token: this.jwtService.sign(payload),
    };
  }

  async logout(token: string): Promise<{ message: string }> {
    // Extract the token from the Authorization header
    const tokenToBlacklist = token.replace('Bearer ', '');
    
    // Add the token to the blacklist
    this.tokenBlacklistService.addToBlacklist(tokenToBlacklist);
    
    return { message: 'Successfully logged out' };
  }
} 