import { Component, OnInit } from '@angular/core';
import { EvenementService } from '../../services/evenement.service';
import { Evenement } from '../../../../core/models';

@Component({
  selector: 'app-evenements',
  templateUrl: './evenements.html',
  styleUrls: ['./evenements.scss'],
  standalone: false
})
export class EvenementsComponent implements OnInit {
  evenements: Evenement[] = [];
  myRegistrations: Evenement[] = [];
  loading = true;

  constructor(private evenementService: EvenementService) {}

  ngOnInit(): void {
    this.loadEvenements();
    this.loadMyRegistrations();
  }

  private loadEvenements(): void {
    this.evenementService.getApprovedEvents().subscribe({
      next: (data) => this.evenements = data,
      error: (err) => console.error(err),
      complete: () => this.loading = false
    });
  }

  private loadMyRegistrations(): void {
    this.evenementService.getMyRegistrations().subscribe({
      next: (data) => this.myRegistrations = data,
      error: (err) => console.error(err)
    });
  }

  register(eventId: number): void {
    this.evenementService.registerForEvent(eventId).subscribe({
      next: (msg) => {
        alert(msg);
        this.loadMyRegistrations();
      },
      error: (err) => alert('Erreur: ' + (err.error?.message || 'Impossible de s\'inscrire'))
    });
  }
  isRegistered(eventId: number): boolean {
  return this.myRegistrations.some(e => e.id === eventId);
}
isFull(event: Evenement): boolean {
  return (event.registeredCount || 0) >= event.capacity;
}
getTotalPlacesRestantes(): number {
  return this.evenements.reduce((sum, e) => sum + (e.capacity - (e.registeredCount || 0)), 0);
}
getCapacityPercent(event: Evenement): number {
  return Math.round(((event.registeredCount || 0) / event.capacity) * 100);
}
getCapacityBarClass(event: Evenement): string {
  const pct = this.getCapacityPercent(event);
  if (pct >= 90) return 'bg-red-500';
  if (pct >= 60) return 'bg-yellow-500';
  return 'bg-green-500';
}
}