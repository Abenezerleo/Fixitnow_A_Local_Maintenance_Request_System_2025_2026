import { Entity, PrimaryGeneratedColumn, Column, ManyToOne, CreateDateColumn, UpdateDateColumn, JoinColumn } from 'typeorm';
import { User } from '../../users/entities/user.entity';
import { Service } from '../../services/entities/service.entity';
import { ServiceType } from '../../auth/enums/service-type.enum';
import { Role } from '../../auth/enums/roles.enum';

export enum RequestStatus {
  PENDING = 'PENDING',
  ACCEPTED = 'ACCEPTED',
  REJECTED = 'REJECTED',
  COMPLETED = 'COMPLETED',
  CANCELLED = 'CANCELLED',
  IN_PROGRESS = 'IN_PROGRESS',

}

@Entity('requests')
export class Request {
  @PrimaryGeneratedColumn()
  request_id: number;

  @Column({
    type: 'enum',
    enum: ServiceType
  })
  serviceType: ServiceType;

  @Column()
  description: string;

  @Column()
  urgency: string;

  @Column('decimal', { precision: 10, scale: 2, nullable: true })
  budget: number | null;

  @Column({
    type: 'enum',
    enum: RequestStatus,
    default: RequestStatus.PENDING
  })
  status: RequestStatus;

  @Column({ type: 'timestamp', nullable: true })
  scheduledDate: Date | null;

  @Column({ type: 'timestamp', nullable: true })
  completionDate: Date | null;

  @Column({ type: 'int', nullable: true })
  rating: number | null;

  @Column({ type: 'text', nullable: true })
  review: string | null;

  // Requester relationship (User with role REQUESTER)
  @ManyToOne(() => User, { eager: true })
  @JoinColumn({ name: 'requester_id' })
  requester: User;

  @Column()
  requester_id: number;

  // Provider relationship (User with role PROVIDER)
  @ManyToOne(() => User, { eager: true, nullable: true })
  @JoinColumn({ name: 'provider_id' })
  provider: User | null;

  @Column({ nullable: true })
  provider_id: number | null;

  // Service relationship
  @ManyToOne(() => Service, { eager: true, nullable: true })
  @JoinColumn({ name: 'service_id' })
  service: Service | null;

  @Column({ nullable: true })
  service_id: number | null;

  @CreateDateColumn()
  createdAt: Date;

  @UpdateDateColumn()
  updatedAt: Date;
} 