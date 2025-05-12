import { Repository } from 'typeorm';
import { ConfigService } from '@nestjs/config';
import { User } from '../../users/entities/user.entity';
import { TokenBlacklistService } from '../token-blacklist.service';
declare const JwtStrategy_base: new (...args: any) => any;
export declare class JwtStrategy extends JwtStrategy_base {
    private readonly userRepository;
    private readonly configService;
    private tokenBlacklistService;
    constructor(userRepository: Repository<User>, configService: ConfigService, tokenBlacklistService: TokenBlacklistService);
    validate(payload: any): Promise<{
        id: any;
        name: any;
        role: any;
    }>;
}
export {};
