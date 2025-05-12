import { Entity, PrimaryGeneratedColumn, Column, ManyToOne, JoinColumn } from 'typeorm';
import { User } from '../../users/entities/user.entity';
import { ServiceType } from '../../auth/enums/service-type.enum';

@Entity('services')
export class Service {
  @PrimaryGeneratedColumn()
  id: number;

  @Column({
    type: 'enum',
    enum: ServiceType
  })
  serviceType: ServiceType;

  @Column('decimal', { precision: 3, scale: 2, default: 0 })
  rating: number;

  @Column('text', { nullable: true })
  images: string; // Store as JSON string

  @ManyToOne(() => User, { eager: true })
  @JoinColumn({ name: 'provider_id' })
  provider: User;

  @Column()
  provider_id: number;
} 