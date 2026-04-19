// core/models/user.ts
export type UserRole = 
  | 'ETUDIANT' 
  | 'ENSEIGNANT' 
  | 'RESPONSABLE_CLUB' 
  | 'ADMINISTRATEUR';

export interface User {
  id: number;
  email: string;
  firstName?: string;
  lastName?: string;
  role: UserRole;
  avatarUrl?: string;
  siId?: string;           // SSO ENICarthage
  isActive: boolean;
  createdAt?: string;
}