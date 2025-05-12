import { Controller, Get, Post, Body, Patch, Param, Delete, UseGuards, Request, Query, ForbiddenException } from '@nestjs/common';
import { UsersService } from './users.service';
import { CreateUserDto } from './dto/create-user.dto';
import { UpdateUserDto } from './dto/update-user.dto';
import { JwtAuthGuard } from '../auth/guards/jwt-auth.guard';
import { RolesGuard } from '../auth/guards/roles.guard';
import { Roles } from '../auth/decorators/roles.decorator';
import { Role } from '../auth/enums/roles.enum';

@Controller('users')
@UseGuards(JwtAuthGuard, RolesGuard)
export class UsersController {
  constructor(private readonly usersService: UsersService) {}

  @Post()
  @Roles(Role.ADMIN)
  create(@Body() createUserDto: CreateUserDto) {
    return this.usersService.create(createUserDto);
  }

  @Get()
  @Roles(Role.ADMIN)
  findAll() {
    return this.usersService.findAll();
  }

  @Get('requesters')
  @Roles(Role.ADMIN)
  getRequesters() {
    return this.usersService.getRequesters();
  }

  @Get('providers')
  @Roles(Role.ADMIN)
  getProviders() {
    return this.usersService.getProviders();
  }

  @Get('total-requesters')
  @Roles(Role.ADMIN)
  getTotalRequesters() {
    return this.usersService.getTotalRequesters();
  }

  @Get('total-providers')
  @Roles(Role.ADMIN )
  getTotalProviders() {
    return this.usersService.getTotalProviders();
  }

  @Get('search-requester')
  @Roles(Role.ADMIN)
  findRequesterByName(@Query('name') name: string) {
    return this.usersService.findRequesterByName(name);
  }

  @Get('search-provider')
  @Roles(Role.ADMIN)
  findProviderByName(@Query('name') name: string) {
    return this.usersService.findProviderByName(name);
  }

  @Get('request-statistics')
  @Roles(Role.ADMIN)
  getRequestStatistics() {
    return this.usersService.getRequestStatistics();
  }

  @Get('total-completed')
  getTotalCompleted() {
    return this.usersService.getTotalCompleted();
  }

  @Get('total-rejected')
  getTotalRejected() {
    return this.usersService.getTotalRejected();
  }

  @Get('total-pending')
  getTotalPending() {
    return this.usersService.getTotalPending();
  }

  @Get('completed-requests')
  @Roles(Role.ADMIN)
  getCompletedRequests() {
    return this.usersService.getCompletedRequests();
  }

  @Get('rejected-requests')
  @Roles(Role.ADMIN)
  getRejectedRequests() {
    return this.usersService.getRejectedRequests();
  }

  @Get('profile')
  getProfile(@Request() req) {
    return this.usersService.getProfile(req.user.id);
  }

  @Get('top-rated-providers')
  getTopRatedProviders() {
    return this.usersService.getTopRatedProviders();
  }

  @Get('provider-performance')
  @Roles(Role.PROVIDER)
  getProviderPerformance(@Request() req) {
    return this.usersService.getProviderPerformance(req.user.id);
  }

  @Get(':id')
  @Roles(Role.ADMIN)
  findOne(@Param('id') id: string) {
    return this.usersService.findOne(+id);
  }

  @Patch(':id')
  update(
    @Param('id') id: string,
    @Body() updateUserDto: UpdateUserDto,
    @Request() req,
  ) {
    // Allow users to update their own profile or admin to update any profile
    if (req.user.role !== Role.ADMIN && req.user.id !== +id) {
      throw new ForbiddenException('You can only update your own profile');
    }
    return this.usersService.update(+id, updateUserDto, req.user);
  }

  @Delete(':id')
  @Roles(Role.ADMIN)
  remove(@Param('id') id: string, @Request() req) {
    return this.usersService.remove(+id, req.user);
  }
} 