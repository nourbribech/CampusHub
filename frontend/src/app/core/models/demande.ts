export interface Demande {
  id: number;
  studentId: number;
  type: 'CERTIFICAT' | 'REMBOURSEMENT' | 'MATERIEL' | 'RESERVATION_SALLE' | 'OTHER';
  detail?: string;
  statut: 'EN_ATTENTE' | 'APPROUVE' | 'REJETE';
  submittedAt: string;
  commentaireAdmin?: string;
}