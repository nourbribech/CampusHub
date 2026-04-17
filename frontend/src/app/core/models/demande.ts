export interface Demande {
  id: number;
  studentId: number;
  type: string;                    // e.g. "CERTIFICAT", "REMBOURSEMENT", "RESERVATION"
  message?: string;
  status: 'PENDING' | 'APPROVED' | 'REJECTED';
  submittedAt: string;
  // optional extra fields you might add later
  adminComment?: string;
  documentUrl?: string;
}

export enum RequestStatus {
  PENDING = 'PENDING',
  APPROVED = 'APPROVED',
  REJECTED = 'REJECTED'
}