export interface Evenement {
  id: number;
  title: string;
  description?: string;
  date: string;               // ISO string or LocalDateTime → string from backend
  location?: string;
  capacity: number;
  status: 'PENDING' | 'APPROVED' | 'REJECTED';
  organizerId: number;
  registeredCount?: number;   // useful for UI
}

export interface EventRegistration {
  id?: number;
  studentId: number;
  eventId: number;
}

export enum EventStatus {
  PENDING = 'PENDING',
  APPROVED = 'APPROVED',
  REJECTED = 'REJECTED'
}