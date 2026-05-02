import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Demande } from '../../../core/models/demande';

@Injectable({
  providedIn: 'root'
})
export class DemandeService {
  private apiUrl = '/api/v1/requests';

  constructor(private http: HttpClient) {}

  submitRequest(request: Partial<Demande>): Observable<Demande> {
    return this.http.post<Demande>(this.apiUrl, request);
  }

  getMyRequests(): Observable<Demande[]> {
    return this.http.get<Demande[]>(`${this.apiUrl}/my`);
  }

  getRequest(id: number): Observable<Demande> {
    return this.http.get<Demande>(`${this.apiUrl}/${id}`);
  }
}