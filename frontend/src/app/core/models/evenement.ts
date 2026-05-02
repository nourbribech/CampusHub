export interface Evenement {
  id: number;
  titre: string;              // was: title ✓ fix this
  description?: string;
  dateDebut: string;          // was: date — backend has dateDebut + dateFin
  dateFin?: string;           // missing entirely
  lieu?: string;              // was: location ✓ fix this
  nbParticipantsMax: number;  // was: capacity ✓ fix this
  statut: 'EN_ATTENTE' | 'OUVERT' | 'PLEIN' | 'ANNULE' | 'TERMINE' | 'REJETE'; // was wrong values
  organisateur?: { id: number; nom: string; prenom: string; }; // was: organizerId (it's a nested object, not an ID)
  imageUrl?: string;
  club?: string;
  commentaireAdmin?: string;
  registeredCount?: number; 
}

export interface EventRegistration {
  id?: number;
  etudiantId: number;
  evenementId: number;
}

export interface EventApplication {
  id: number;
  evenementId: number;
  etudiantId: number;
  statut: 'EN_ATTENTE' | 'APPROUVE' | 'REJETE';
  dateDemande?: string;
  commentaireAdmin?: string;
}

export enum EventStatus {
  PENDING = 'EN_ATTENTE',
  APPROVED = 'APPROUVE',
  REJECTED = 'REJETE'
}