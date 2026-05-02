import { Component, OnInit } from '@angular/core';
import { ClubService } from '../../services/club.service';
import { EvenementService } from '../../services/evenement.service';
import { DemandeService } from '../../services/demande.service';
import { Club, Evenement, Demande } from '../../../../core/models';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.html',
  styleUrls: ['./dashboard.scss'],
  standalone: false
})
export class DashboardComponent implements OnInit {
  clubs: Club[] = [];
  evenements: Evenement[] = [];
  demandes: Demande[] = [];
  loading = true;

  constructor(
    private clubService: ClubService,
    private evenementService: EvenementService,
    private demandeService: DemandeService
  ) {}

  ngOnInit(): void {
    this.loadDashboardData();
  }

  private loadDashboardData(): void {
    Promise.all([
      this.clubService.getActiveClubs().toPromise().catch(() => []),
      this.evenementService.getApprovedEvents().toPromise().catch(() => []),
      this.demandeService.getMyRequests().toPromise().catch(() => [])
    ]).then(([clubs, evenements, demandes]) => {
      this.clubs = clubs?.slice(0, 3) || [];
      this.evenements = evenements?.slice(0, 3) || [];
      this.demandes = demandes?.slice(0, 3) || [];
    }).finally(() => {
      this.loading = false;
    });
  }

  register(eventId: number): void {
    this.evenementService.registerForEvent(eventId).subscribe({
      next: (msg) => alert(msg),
      error: (err) => alert('Erreur: ' + (err.error?.message || 'Impossible de s\'inscrire'))
    });
  }
}