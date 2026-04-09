import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

const routes: Routes = [
  {
    path: '',
    redirectTo: 'dashboard',
    pathMatch: 'full'
  },
  {
    path: 'dashboard',
    loadComponent: () => import('./pages/dashboard/dashboard')
      .then(m => m.DashboardComponent)
  },
  {
    path: 'reservation-salle',
    loadComponent: () => import('./pages/reservation-salle/reservation-salle')
      .then(m => m.ReservationSalle)
  },
  {
    path: 'mes-reservations',
    loadComponent: () => import('./pages/mes-reservations/mes-reservations')
      .then(m => m.MesReservations)
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class EnseignantRoutingModule { }