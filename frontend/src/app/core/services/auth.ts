import { Injectable, PLATFORM_ID, Inject, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { isPlatformBrowser } from '@angular/common';
import { environment } from '../../../environments/environment';

export interface User {
  id: number;
  nom: string;
  prenom: string;
  email: string;
  role: 'ETUDIANT' | 'ENSEIGNANT' | 'RESPONSABLE_CLUB' | 'MODERATEUR' | 'ADMIN';
  token: string;
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

@Injectable({ providedIn: 'root' })
export class AuthService {
  private http = inject(HttpClient);
  private apiUrl = `${environment.apiUrl}/auth`;
  private isBrowser: boolean;
  private currentUserSubject = new BehaviorSubject<User | null>(null);
  currentUser$ = this.currentUserSubject.asObservable();

  constructor(@Inject(PLATFORM_ID) platformId: Object) {
    this.isBrowser = isPlatformBrowser(platformId);
    if (this.isBrowser) {
      const stored = localStorage.getItem('campushub_user');
      if (stored) this.currentUserSubject.next(JSON.parse(stored));
    }
  }

  get currentUser(): User | null {
    return this.currentUserSubject.value;
  }

  get isLoggedIn(): boolean {
    return !!this.currentUser;
  }

  get isAdmin(): boolean {
    return this.currentUser?.role === 'ADMIN';
  }

  login(credentials: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.apiUrl}/login`, credentials).pipe(
      tap(response => {
        if (this.isBrowser) {
          localStorage.setItem('campushub_user', JSON.stringify(response.user));
          localStorage.setItem('campushub_token', response.token);
        }
        this.currentUserSubject.next(response.user);
      })
    );
  }

  logout(): void {
    if (this.isBrowser) {
      localStorage.removeItem('campushub_user');
      localStorage.removeItem('campushub_token');
    }
    this.currentUserSubject.next(null);
    // Use window.location to avoid Router circular dependency
    if (this.isBrowser) {
      window.location.href = '/auth/login';
    }
  }

  getToken(): string | null {
    return this.isBrowser ? localStorage.getItem('campushub_token') : null;
  }
}