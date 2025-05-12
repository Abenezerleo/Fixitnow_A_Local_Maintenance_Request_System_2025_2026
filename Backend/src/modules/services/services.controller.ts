import { Controller, Get, Post, Body, Patch, Param, Delete, UseGuards, Request } from '@nestjs/common';
import { ServicesService } from './services.service';
import { JwtAuthGuard } from '../auth/guards/jwt-auth.guard';
import { RolesGuard } from '../auth/guards/roles.guard';
import { Roles } from '../auth/decorators/roles.decorator';
import { Role } from '../auth/enums/roles.enum';

@Controller('services')
export class ServicesController {
  constructor(private readonly servicesService: ServicesService) {}

  @Post()
  @UseGuards(JwtAuthGuard, RolesGuard)
  @Roles(Role.PROVIDER)
  create(@Body() createServiceDto: any, @Request() req) {
    return this.servicesService.create(createServiceDto, req.user.id);
  }

  @Get()
  findAll() {
    return this.servicesService.findAll();
  }

  @Get('my-services')
  @UseGuards(JwtAuthGuard, RolesGuard)
  @Roles(Role.PROVIDER)
  findMyServices(@Request() req) {
    return this.servicesService.findByProvider(req.user.id);
  }

  @Get(':id')
  findOne(@Param('id') id: string) {
    return this.servicesService.findOne(+id);
  }

  @Patch(':id')
  @UseGuards(JwtAuthGuard, RolesGuard)
  @Roles(Role.PROVIDER)
  update(@Param('id') id: string, @Body() updateServiceDto: any) {
    return this.servicesService.update(+id, updateServiceDto);
  }

  @Delete(':id')
  @UseGuards(JwtAuthGuard, RolesGuard)
  @Roles(Role.PROVIDER)
  remove(@Param('id') id: string) {
    return this.servicesService.remove(+id);
  }

  @Patch(':id/rating')
  @UseGuards(JwtAuthGuard, RolesGuard)
  @Roles(Role.REQUESTER)
  updateRating(@Param('id') id: string, @Body('rating') rating: number) {
    return this.servicesService.updateRating(+id, rating);
  }
} 