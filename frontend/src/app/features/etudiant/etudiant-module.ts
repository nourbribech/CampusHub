import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';   

import { EtudiantRoutingModule } from './etudiant-routing-module';

import { DashboardComponent } from './pages/dashboard/dashboard';
import { ClubsComponent } from './pages/clubs/clubs';
import { EvenementsComponent } from './pages/evenements/evenements';
import { MesDemandesComponent } from './pages/mes-demandes/mes-demandes';

import { ClubService } from './services/club.service';
import { EvenementService } from './services/evenement.service';
import { DemandeService } from './services/demande.service';

import { Header } from '../../shared/components/header/header';
@NgModule({
  declarations: [
    DashboardComponent,
    ClubsComponent,
    EvenementsComponent,
    MesDemandesComponent
  ],
  imports: [
    CommonModule,                    
    EtudiantRoutingModule,Header
  ],
  providers: [
    ClubService,
    EvenementService,
    DemandeService
  ]
})
export class EtudiantModule { }