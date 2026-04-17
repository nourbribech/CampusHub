export interface Club {
  id: number;
  name: string;
  description?: string;
  category?: string;
  status: 'ACTIVE' | 'INACTIVE';
  headId: number;
  createdAt?: string;
}

export interface ClubApplication {
  id: number;
  studentId: number;
  clubId: number;
  motivation: string;
  status: 'PENDING' | 'ACCEPTED' | 'REJECTED';
  submittedAt?: string;
}

export enum ClubStatus {
  ACTIVE = 'ACTIVE',
  INACTIVE = 'INACTIVE'
}

export enum ApplicationStatus {
  PENDING = 'PENDING',
  ACCEPTED = 'ACCEPTED',
  REJECTED = 'REJECTED'
}