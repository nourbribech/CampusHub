export interface Reservation {
  id: string;
  dateDebut: string;
  dateFin: string;
  statut: 'EN_ATTENTE' | 'APPROUVEE' | 'REFUSEE' | 'ANNULEE';
  salle?: string;
  description?: string;
}
