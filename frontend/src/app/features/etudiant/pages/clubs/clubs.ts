import { Component, OnInit } from '@angular/core';
import { ClubService } from '../../services/club.service';
import { Club, ClubApplication } from '../../../../core/models';
@Component({
  selector: 'app-clubs',
  templateUrl: './clubs.html',
  styleUrls: ['./clubs.scss'],
  standalone: false
})
export class ClubsComponent implements OnInit {
  clubs: Club[] = [];
  myApplications: ClubApplication[] = [];
  loading = true;

  constructor(private clubService: ClubService) {}

  ngOnInit(): void {
    this.loadClubs();
    this.loadMyApplications();
  }

  private loadClubs(): void {
    this.clubService.getActiveClubs().subscribe({
      next: (data) => this.clubs = data,
      error: (err) => console.error(err),
      complete: () => this.loading = false
    });
  }

  private loadMyApplications(): void {
    this.clubService.getMyApplications().subscribe({
      next: (data) => this.myApplications = data,
      error: (err) => console.error(err)
    });
  }

  applyToClub(clubId: number, motivation: string): void {
    this.clubService.applyToClub(clubId, motivation).subscribe({
      next: (msg) => {
        alert(msg);
        this.loadMyApplications();
      },
      error: (err) => alert('Erreur: ' + (err.error?.message || 'Impossible de candidater'))
    });
  }
  hasApplied(clubId: number): boolean {
  return this.myApplications.some(a => a.clubId === clubId);
}
getApplicationStatusClass(clubId: number): string {
  const app = this.myApplications.find(a => a.clubId === clubId);
  const map: Record<string, string> = {
    APPROUVE: 'bg-green-100 text-green-700',
    REFUSE: 'bg-red-100 text-red-700',
    EN_ATTENTE: 'bg-yellow-100 text-yellow-700'
  };
  return app ?( map[app.statut] ??'' ): '';
}
getApplicationStatusLabel(clubId: number): string {
  const app = this.myApplications.find(a => a.clubId === clubId);
  const map: Record<string, string> = { ACCEPTED: '✅ Accepté', REJECTED: '❌ Rejeté', PENDING: '⏳ En attente' };
  return app ? ( map[app.statut] ??'' ) : '';
}
getAcceptedCount(): number {
  return this.myApplications.filter(app => app.statut === 'APPROUVE').length;
}
}