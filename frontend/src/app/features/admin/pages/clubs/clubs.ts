import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-clubs',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './clubs.html'
})
export class Clubs {
  clubs = [
    { nom: 'Club Robotique', responsable: 'Karim Mansour', membres: 45, status: 'ACTIF', categorie: 'Technologie' },
    { nom: 'Club Entrepreneuriat', responsable: 'Sonia Trabelsi', membres: 32, status: 'ACTIF', categorie: 'Business' },
    { nom: 'Club Dev', responsable: 'Ahmed Ben Ali', membres: 28, status: 'ACTIF', categorie: 'Technologie' },
    { nom: 'Club IA', responsable: 'Leila Bouzid', membres: 15, status: 'EN_ATTENTE', categorie: 'Technologie' },
    { nom: 'Club Arts', responsable: 'Mohamed Sassi', membres: 20, status: 'ACTIF', categorie: 'Culture' },
  ];

  approuver(club: any): void { club.status = 'ACTIF'; }
  suspendre(club: any): void { club.status = 'SUSPENDU'; }

  getStatusClass(status: string): string {
    const classes: any = {
      'ACTIF': 'bg-green-100 text-green-700',
      'EN_ATTENTE': 'bg-yellow-100 text-yellow-700',
      'SUSPENDU': 'bg-red-100 text-red-700',
    };
    return classes[status] || 'bg-gray-100 text-gray-700';
  }
}