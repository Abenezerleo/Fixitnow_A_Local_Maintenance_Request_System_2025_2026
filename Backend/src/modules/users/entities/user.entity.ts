import { Entity, Column, PrimaryGeneratedColumn } from 'typeorm';
import { Role } from '../../auth/enums/roles.enum';
import { ServiceType } from '../../auth/enums/service-type.enum';

@Entity()
export class User {
  @PrimaryGeneratedColumn()
  id: number;

  @Column()
  name: string;

  @Column({ unique: true })
  email: string;

  @Column()
  phone: string;

  @Column()
  password: string;

  @Column({
    type: 'enum',
    enum: Role,
    default: Role.REQUESTER
  })
  role: Role;

  @Column()
  gender: string;

  @Column({
    type: 'enum',
    enum: ServiceType,
    nullable: true
  })
  serviceType?: ServiceType;

  // Payment accounts
  @Column({ nullable: true })
  cbeAccount?: string;

  @Column({ nullable: true })
  paypalAccount?: string;

  @Column({ nullable: true })
  telebirrAccount?: string;

  @Column({ nullable: true })
  awashAccount?: string;

  // Additional fields
  @Column({ nullable: true })
  age?: number;

  @Column({ nullable: true })
  height?: number;

  @Column({ nullable: true })
  weight?: number;

  @Column({ nullable: true })
  joinDate?: string;

  @Column({ nullable: true })
  membershipStatus?: string;

  // Provider specific fields
  @Column({ type: 'float', nullable: true, default: 0 })
  providerRating?: number;

  @Column({ type: 'int', nullable: true, default: 0 })
  totalJobsCompleted?: number;

  @Column({ type: 'float', nullable: true, default: 0 })
  totalIncome?: number;
} 