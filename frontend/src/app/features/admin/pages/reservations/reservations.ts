import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-reservations',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './reservations.html'
})
export class Reservations {
  reservations = [
    { salle: 'Amphi A', demandeur: 'Dr. Trabelsi', date: '20 Avr 2025', heure: '09:00 - 11:00', motif: 'Cours magistral', status: 'EN_ATTENTE' },
    { salle: 'Labo Info 2', demandeur: 'Club Robotique', date: '21 Avr 2025', heure: '14:00 - 17:00', motif: 'Atelier IA', status: 'APPROUVEE' },
    { salle: 'Salle B12', demandeur: 'Dr. Mansour', date: '22 Avr 2025', heure: '10:00 - 12:00', motif: 'TD Réseaux', status: 'EN_ATTENTE' },
  ];

  approuver(r: any): void { r.status = 'APPROUVEE'; }
  rejeter(r: any): void { r.status = 'REJETEE'; }
}
