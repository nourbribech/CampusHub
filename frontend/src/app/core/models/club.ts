export interface Club {
  id: number;
  nom: string;
  description?: string;
  categorie?: string;
  statut: 'EN_ATTENTE' | 'ACTIF' | 'SUSPENDU';
  responsableClubId: number;
  dateCreation?: string;
}

export interface ClubApplication {
  id: number;
  studentId: number;
  clubId: number;
  motivation: string;
  statut: 'EN_ATTENTE' | 'APPROUVE' | 'REJETE';
  dateDemande?: string;
}

export enum ClubStatus {
  PENDING = 'EN_ATTENTE',
  ACTIVE = 'ACTIF',
  SUSPENDED = 'SUSPENDU'
}

export enum ApplicationStatus {
  PENDING = 'EN_ATTENTE',
  ACCEPTED = 'APPROUVE',
  REJECTED = 'REJETE'
}