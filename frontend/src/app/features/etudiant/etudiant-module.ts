import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';   // ← THIS WAS MISSING / NOT SAVED

import { EtudiantRoutingModule } from './etudiant-routing-module';

import { DashboardComponent } from './pages/dashboard/dashboard';
import { ClubsComponent } from './pages/clubs/clubs';
import { EvenementsComponent } from './pages/evenements/evenements';
import { MesDemandesComponent } from './pages/mes-demandes/mes-demandes';

import { ClubService } from './services/club.service';
import { EvenementService } from './services/evenement.service';
import { DemandeService } from './services/demande.service';

@NgModule({
  declarations: [
    DashboardComponent,
    ClubsComponent,
    EvenementsComponent,
    MesDemandesComponent
  ],
  imports: [
    CommonModule,                    // ← MUST BE HERE
    EtudiantRoutingModule
  ],
  providers: [
    ClubService,
    EvenementService,
    DemandeService
  ]
})
export class EtudiantModule { }