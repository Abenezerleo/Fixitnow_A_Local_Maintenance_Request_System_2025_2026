import { Controller, Get, Post, Body, Patch, Param, Delete, UseGuards, Request } from '@nestjs/common';
import { RequestsService } from './requests.service';
import { CreateRequestDto } from './dto/create-request.dto';
import { UpdateRequestDto } from './dto/update-request.dto';
import { JwtAuthGuard } from '../auth/guards/jwt-auth.guard';
import { RolesGuard } from '../auth/guards/roles.guard';
import { Roles } from '../auth/decorators/roles.decorator';
import { Role } from '../auth/enums/roles.enum';
import { RequestStatus } from './entities/request.entity';

@Controller('requests')
@UseGuards(JwtAuthGuard)
export class RequestsController {
  constructor(private readonly requestsService: RequestsService) {}

  @Post()
  @UseGuards(RolesGuard)
  @Roles(Role.REQUESTER)
  create(@Body() createRequestDto: CreateRequestDto, @Request() req) {
    return this.requestsService.create(createRequestDto, req.user.id);
  }
  
  @Get('completed-requests')
  @Roles(Role.ADMIN)
  getCompletedRequests() {
    return this.requestsService.getCompletedRequests();
  }

  @Get('rejected-requests')
  @Roles(Role.ADMIN)
  getRejectedRequests() {
    return this.requestsService.getRejectedRequests();
  }


  @Get()
  @UseGuards(RolesGuard)
  @Roles(Role.ADMIN)
  findAll() {
    return this.requestsService.findAll();
  }

  @Get('my-requests')
  @UseGuards(RolesGuard)
  @Roles(Role.REQUESTER)
  findMyRequests(@Request() req) {
    return this.requestsService.findByRequester(req.user.id);
  }

  @Get('provider-requests')
  @UseGuards(RolesGuard)
  @Roles(Role.PROVIDER)
  findProviderRequests(@Request() req) {
    return this.requestsService.findByProvider(req.user.id);
  }

  @Get('provider-completed')
  @UseGuards(RolesGuard)
  @Roles(Role.PROVIDER)
  findProviderCompletedRequests(@Request() req) {
    return this.requestsService.getProviderCompletedRequests(req.user.id);
  }

  @Get('provider-accepted')
  @UseGuards(RolesGuard)
  @Roles(Role.PROVIDER)
  findProviderAcceptedRequests(@Request() req) {
    return this.requestsService.getProviderAcceptedRequests(req.user.id);
  }

  @Get('provider-stats/completed-count')
  @UseGuards(RolesGuard)
  @Roles(Role.PROVIDER)
  getProviderCompletedCount(@Request() req) {
    return this.requestsService.getProviderCompletedRequestCount(req.user.id);
  }

  @Get('provider-stats/average-rating')
  @UseGuards(RolesGuard)
  @Roles(Role.PROVIDER)
  getProviderAverageRating(@Request() req) {
    return this.requestsService.getProviderAverageRating(req.user.id);
  }

  @Get('provider-stats/total-budget')
  @UseGuards(RolesGuard)
  @Roles(Role.PROVIDER)
  getProviderTotalBudget(@Request() req) {
    return this.requestsService.getProviderTotalBudget(req.user.id);
  }
  
  @Get('unassigned')
  @UseGuards(RolesGuard)
  @Roles(Role.PROVIDER)
  findUnassignedRequests() {
    return this.requestsService.findUnassignedRequests();
  }

  @Get(':id')
  findOne(@Param('id') id: string) {
    return this.requestsService.findOne(+id);
  }

  @Patch(':id/accept')
  @UseGuards(RolesGuard)
  @Roles(Role.PROVIDER)
  acceptRequest(@Param('id') id: string, @Request() req) {
    return this.requestsService.acceptRequest(+id, req.user.id);
  }

  @Patch(':id/status')
  updateStatus(
    @Param('id') id: string,
    @Body('status') status: RequestStatus,
    @Request() req
  ) {
    return this.requestsService.updateStatus(+id, status, req.user.id);
  }

  @Patch(':id')
  update(
    @Param('id') id: string,
    @Body() updateRequestDto: UpdateRequestDto,
    @Request() req
  ) {
    return this.requestsService.update(+id, updateRequestDto, req.user.id);
  }

  @Delete(':id')
  remove(@Param('id') id: string, @Request() req) {
    return this.requestsService.remove(+id, req.user.id);
  }

  @Patch(':id/review')
  @UseGuards(RolesGuard)
  @Roles(Role.REQUESTER)
  addReview(
    @Param('id') id: string,
    @Body('rating') rating: number,
    @Body('review') review: string,
    @Request() req
  ) {
    return this.requestsService.addReview(+id, rating, review, req.user.id);
  }

} 