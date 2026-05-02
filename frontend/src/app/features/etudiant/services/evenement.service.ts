import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Evenement, EventRegistration } from '../../../core/models/evenement';

@Injectable({
  providedIn: 'root'
})
export class EvenementService {
  private apiUrl = '/api/v1/events';   // matches your backend

  constructor(private http: HttpClient) {}

  getApprovedEvents(): Observable<Evenement[]> {
    return this.http.get<Evenement[]>(`${this.apiUrl}/approved`);
  }

  registerForEvent(eventId: number): Observable<string> {
    return this.http.post<string>(`${this.apiUrl}/${eventId}/register`, {});
  }

  getMyRegistrations(): Observable<Evenement[]> {
    return this.http.get<Evenement[]>(`${this.apiUrl}/my-registrations`);
  }
  isOpen(e: Evenement): boolean {
  return e.statut === 'OUVERT';
}

isFull(e: Evenement): boolean {
  return e.statut === 'PLEIN';
}
}