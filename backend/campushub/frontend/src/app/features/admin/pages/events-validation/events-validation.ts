import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-events-validation',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './events-validation.html'
})
export class EventsValidation {
  events = [
    { titre: 'Hackathon IA 2025', club: 'Club Robotique', date: '20 Avr 2025', lieu: 'Amphi A', participants: 50, status: 'EN_ATTENTE' },
    { titre: 'Soirée networking', club: 'Club Entrepreneuriat', date: '25 Avr 2025', lieu: 'Salle B12', participants: 30, status: 'EN_ATTENTE' },
    { titre: 'Workshop Angular', club: 'Club Dev', date: '28 Avr 2025', lieu: 'Labo Info', participants: 25, status: 'EN_ATTENTE' },
  ];

  approuver(event: any): void {
    event.status = 'APPROUVE';
  }

  rejeter(event: any): void {
    event.status = 'REJETE';
  }
}
