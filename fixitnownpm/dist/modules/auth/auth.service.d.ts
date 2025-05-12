import { JwtService } from '@nestjs/jwt';
import { Repository } from 'typeorm';
import { User } from '../users/entities/user.entity';
import { RegisterDto } from './dto/register.dto';
import { LoginDto } from './dto/login.dto';
import { TokenBlacklistService } from './token-blacklist.service';
export declare class AuthService {
    private userRepository;
    private jwtService;
    private tokenBlacklistService;
    constructor(userRepository: Repository<User>, jwtService: JwtService, tokenBlacklistService: TokenBlacklistService);
    register(registerDto: RegisterDto): Promise<User>;
    login(loginDto: LoginDto): Promise<{
        access_token: string;
    }>;
    logout(token: string): Promise<{
        message: string;
    }>;
}
