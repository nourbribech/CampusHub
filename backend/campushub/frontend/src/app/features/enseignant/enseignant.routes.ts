import { Routes } from '@angular/router';

export const ENSEIGNANT_ROUTES: Routes = [
  {
    path: '',
    redirectTo: 'dashboard',
    pathMatch: 'full'
  },
  {
    path: 'dashboard',
    loadComponent: () => import('./pages/dashboard/dashboard').then(m => m.DashboardComponent)
  },
  {
    path: 'reservation-salle',
    loadComponent: () => import('./pages/reservation-salle/reservation-salle').then(m => m.ReservationSalle)
  },
  {
    path: 'mes-reservations',
    loadComponent: () => import('./pages/mes-reservations/mes-reservations').then(m => m.MesReservations)
  },
  {
    path: 'calendrier',
    loadComponent: () => import('./pages/calendrier/calendrier').then(m => m.Calendrier)
  }
];
