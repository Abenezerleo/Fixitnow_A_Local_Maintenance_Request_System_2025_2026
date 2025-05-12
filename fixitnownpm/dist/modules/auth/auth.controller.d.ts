import { AuthService } from './auth.service';
import { RegisterDto } from './dto/register.dto';
import { LoginDto } from './dto/login.dto';
export declare class AuthController {
    private readonly authService;
    constructor(authService: AuthService);
    register(registerDto: RegisterDto): Promise<import("../users/entities/user.entity").User>;
    login(loginDto: LoginDto): Promise<{
        access_token: string;
    }>;
    logout(authHeader: string): Promise<{
        message: string;
    }>;
    getProfile(req: any): any;
}
