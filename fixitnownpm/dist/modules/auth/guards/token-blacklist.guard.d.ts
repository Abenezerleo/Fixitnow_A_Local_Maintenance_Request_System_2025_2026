import { CanActivate, ExecutionContext } from '@nestjs/common';
import { TokenBlacklistService } from '../token-blacklist.service';
export declare class TokenBlacklistGuard implements CanActivate {
    private tokenBlacklistService;
    constructor(tokenBlacklistService: TokenBlacklistService);
    canActivate(context: ExecutionContext): boolean;
    private extractTokenFromHeader;
}
