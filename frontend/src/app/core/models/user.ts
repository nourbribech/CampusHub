// src/app/core/models/user.ts
export interface User {
    id: number;
    nom: string;
    prenom: string;
    email: string;
    role: 'ETUDIANT' | 'ENSEIGNANT' | 'RESPONSABLE_CLUB' | 'MODERATEUR' | 'ADMIN';
    avatar?: string;
    refreshToken?: string;
}

export interface LoginRequest {
    email: string;
    password: string;
}

export interface LoginResponse {
    token: string;
    refreshToken: string;
    user: User;
}