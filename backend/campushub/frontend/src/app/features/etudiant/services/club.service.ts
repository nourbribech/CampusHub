import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Club, ClubApplication } from '../../../core/models';

@Injectable({
  providedIn: 'root'
})
export class ClubService {
  private apiUrl = '/api/v1/clubs';

  constructor(private http: HttpClient) {}

  // Student features
  getActiveClubs(): Observable<Club[]> {
    return this.http.get<Club[]>(`${this.apiUrl}/active`);
  }

  getClub(id: number): Observable<Club> {
    return this.http.get<Club>(`${this.apiUrl}/${id}`);
  }

  applyToClub(clubId: number, motivation: string): Observable<string> {
    return this.http.post<string>(`${this.apiUrl}/${clubId}/apply`, { motivation });
  }

  getMyApplications(): Observable<ClubApplication[]> {
    return this.http.get<ClubApplication[]>(`${this.apiUrl}/my-applications`);
  }

  // Club head features (if you want to show them later)
  getApplicationsForMyClub(clubId: number): Observable<ClubApplication[]> {
    return this.http.get<ClubApplication[]>(`${this.apiUrl}/${clubId}/applications`);
  }
}