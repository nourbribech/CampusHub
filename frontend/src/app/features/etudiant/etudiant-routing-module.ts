import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DashboardComponent } from './pages/dashboard/dashboard';
import { ClubsComponent } from './pages/clubs/clubs';
import { EvenementsComponent } from './pages/evenements/evenements';
import { MesDemandesComponent } from './pages/mes-demandes/mes-demandes';

const routes: Routes = [
  { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
  { path: 'dashboard',    component: DashboardComponent },
  { path: 'clubs',        component: ClubsComponent },
  { path: 'evenements',   component: EvenementsComponent },
  { path: 'mes-demandes', component: MesDemandesComponent }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class EtudiantRoutingModule { }