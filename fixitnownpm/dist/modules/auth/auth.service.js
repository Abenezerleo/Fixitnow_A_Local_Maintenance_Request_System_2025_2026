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
exports.AuthService = void 0;
const common_1 = require("@nestjs/common");
const jwt_1 = require("@nestjs/jwt");
const typeorm_1 = require("@nestjs/typeorm");
const typeorm_2 = require("typeorm");
const user_entity_1 = require("../users/entities/user.entity");
const roles_enum_1 = require("./enums/roles.enum");
const bcrypt = require("bcrypt");
const token_blacklist_service_1 = require("./token-blacklist.service");
let AuthService = class AuthService {
    userRepository;
    jwtService;
    tokenBlacklistService;
    constructor(userRepository, jwtService, tokenBlacklistService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.tokenBlacklistService = tokenBlacklistService;
    }
    async register(registerDto) {
        const { email, password, role, serviceType, name, phone, gender } = registerDto;
        const existingUser = await this.userRepository.findOne({ where: { email } });
        if (existingUser) {
            throw new common_1.BadRequestException('Email already registered');
        }
        const existingUsername = await this.userRepository.findOne({ where: { name } });
        if (existingUsername) {
            throw new common_1.BadRequestException('Username already taken');
        }
        if (role === roles_enum_1.Role.PROVIDER && !serviceType) {
            throw new common_1.BadRequestException('Service type is required for providers');
        }
        const hashedPassword = await bcrypt.hash(password, 10);
        const user = new user_entity_1.User();
        Object.assign(user, {
            ...registerDto,
            password: hashedPassword,
            serviceType: role === roles_enum_1.Role.PROVIDER ? serviceType : null,
            providerRating: role === roles_enum_1.Role.PROVIDER ? 0 : null,
            totalJobsCompleted: role === roles_enum_1.Role.PROVIDER ? 0 : null,
            totalIncome: role === roles_enum_1.Role.PROVIDER ? 0 : null,
        });
        return this.userRepository.save(user);
    }
    async login(loginDto) {
        const { name, password } = loginDto;
        const user = await this.userRepository.findOne({ where: { name: name } });
        if (!user) {
            throw new common_1.UnauthorizedException('Invalid credentials');
        }
        const isPasswordValid = await bcrypt.compare(password, user.password);
        if (!isPasswordValid) {
            throw new common_1.UnauthorizedException('Invalid credentials');
        }
        if (!user.id) {
            throw new common_1.UnauthorizedException('Invalid user data');
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
    async logout(token) {
        const tokenToBlacklist = token.replace('Bearer ', '');
        this.tokenBlacklistService.addToBlacklist(tokenToBlacklist);
        return { message: 'Successfully logged out' };
    }
};
exports.AuthService = AuthService;
exports.AuthService = AuthService = __decorate([
    (0, common_1.Injectable)(),
    __param(0, (0, typeorm_1.InjectRepository)(user_entity_1.User)),
    __metadata("design:paramtypes", [typeorm_2.Repository,
        jwt_1.JwtService,
        token_blacklist_service_1.TokenBlacklistService])
], AuthService);
//# sourceMappingURL=auth.service.js.map