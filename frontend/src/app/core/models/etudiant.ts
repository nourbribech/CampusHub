// core/models/etudiant.ts
import { User } from './user';

export interface Etudiant extends User {
  matricule: string;
  filiere: string;
  promotion: number;
  badges?: string[];
}