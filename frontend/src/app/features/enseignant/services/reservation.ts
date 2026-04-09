import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';

export interface Salle {
  id: string;
  nom: string;
  capacite: number;
  equipements: string[];
  batiment: string;
  etage: number;
  disponible: boolean;
  imageUrl?: string;
}

export interface User {
  id: string;
  nom: string;
  prenom: string;
  email: string;
  role: string;
  departement?: string;
  avatar?: string;
}

export interface Reservation {
  id?: string;
  salleId: string;
  salle?: Salle;
  enseignantId: string;
  enseignant?: User;
  dateDebut: Date | string;
  dateFin: Date | string;
  motif: string;
  nombreParticipants?: number;
  statut: 'EN_ATTENTE' | 'APPROUVEE' | 'REJETEE';
  commentaireAdmin?: string;
  createdAt?: Date;
}

export interface CreateReservationDto {
  salleId: string;
  dateDebut: string;
  dateFin: string;
  motif: string;
  nombreParticipants?: number;
}

@Injectable({
  providedIn: 'root'
})
export class ReservationService {
  private apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) { }

  // Récupérer toutes les salles
  getSalles(): Observable<Salle[]> {
    return this.http.get<Salle[]>(`${this.apiUrl}/salles`);
  }

  // Récupérer les salles disponibles
  getSallesDisponibles(dateDebut: string, dateFin: string): Observable<Salle[]> {
    return this.http.get<Salle[]>(
      `${this.apiUrl}/salles/disponibles?dateDebut=${dateDebut}&dateFin=${dateFin}`
    );
  }

  // Récupérer mes réservations
  getMesReservations(): Observable<Reservation[]> {
    return this.http.get<Reservation[]>(`${this.apiUrl}/reservations/me`);
  }

  // Récupérer une réservation par ID
  getReservationById(id: string): Observable<Reservation> {
    return this.http.get<Reservation>(`${this.apiUrl}/reservations/${id}`);
  }

  // Créer une réservation
  creerReservation(reservation: CreateReservationDto): Observable<Reservation> {
    return this.http.post<Reservation>(`${this.apiUrl}/reservations`, reservation);
  }

  // Annuler une réservation
  annulerReservation(id: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/reservations/${id}`);
  }

  // Modifier une réservation
  modifierReservation(id: string, reservation: Partial<Reservation>): Observable<Reservation> {
    return this.http.put<Reservation>(`${this.apiUrl}/reservations/${id}`, reservation);
  }
}