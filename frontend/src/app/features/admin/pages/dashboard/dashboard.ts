import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './dashboard.html'
})
export class Dashboard {
  kpis = [
    { label: 'Utilisateurs actifs', value: '1,248', icon: '👥', color: 'bg-blue-50 text-blue-600' },
    { label: 'Événements ce mois', value: '34', icon: '📅', color: 'bg-green-50 text-green-600' },
    { label: 'Demandes en attente', value: '12', icon: '📋', color: 'bg-yellow-50 text-yellow-600' },
    { label: 'Réservations salles', value: '89', icon: '🏫', color: 'bg-purple-50 text-purple-600' },
  ];

  pendingEvents = [
    { titre: 'Hackathon IA 2025', club: 'Club Robotique', date: '20 Avr 2025', status: 'en_attente' },
    { titre: 'Soirée networking', club: 'Club Entrepreneuriat', date: '25 Avr 2025', status: 'en_attente' },
    { titre: 'Workshop Angular', club: 'Club Dev', date: '28 Avr 2025', status: 'en_attente' },
  ];
}
