import { Injectable, CanActivate, ExecutionContext, UnauthorizedException } from '@nestjs/common';
import { TokenBlacklistService } from '../token-blacklist.service';

@Injectable()
export class TokenBlacklistGuard implements CanActivate {
  constructor(private tokenBlacklistService: TokenBlacklistService) {}

  canActivate(context: ExecutionContext): boolean {
    const request = context.switchToHttp().getRequest();
    const token = this.extractTokenFromHeader(request);
    
    if (!token) {
      throw new UnauthorizedException('No token provided');
    }

    if (this.tokenBlacklistService.isBlacklisted(token)) {
      throw new UnauthorizedException('Token has been invalidated');
    }

    return true;
  }

  private extractTokenFromHeader(request: any): string | undefined {
    const [type, token] = request.headers.authorization?.split(' ') ?? [];
    return type === 'Bearer' ? token : undefined;
  }
} 