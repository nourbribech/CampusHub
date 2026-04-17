import { Component, OnInit } from '@angular/core';
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
  loading = false;   // ← immediately false

  ngOnInit(): void {
    // Mock data so you can see the UI
    this.clubs = [
      { id: 1, name: 'Club Robotique', description: 'Concours nationaux', status: 'ACTIVE', headId: 1 },
      { id: 2, name: 'ENIC Club', description: 'Événements tech', status: 'ACTIVE', headId: 2 }
    ];
    this.evenements = [
      { id: 1, title: 'Hackathon 2026', description: '48h de coding', date: '2026-05-10', status: 'APPROVED', capacity: 50, organizerId: 1 },
      { id: 2, title: 'Conférence IA', description: 'Avec experts', date: '2026-04-25', status: 'APPROVED', capacity: 120, organizerId: 2 }
    ];
    this.demandes = [
      { id: 1, type: 'CERTIFICAT', message: 'Demande de stage', status: 'PENDING', submittedAt: '2026-04-15' }
    ];
  }

  register(eventId: number): void {
    alert(`✅ Inscription à l'événement ${eventId} simulée avec succès !`);
  }
}