import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-users',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './users.html'
})
export class Users {
  users = [
    { nom: 'Ben Ali', prenom: 'Ahmed', email: 'ahmed.benali@enicarthage.tn', role: 'ETUDIANT', status: 'actif' },
    { nom: 'Trabelsi', prenom: 'Sonia', email: 'sonia.trabelsi@enicarthage.tn', role: 'ENSEIGNANT', status: 'actif' },
    { nom: 'Mansour', prenom: 'Karim', email: 'karim.mansour@enicarthage.tn', role: 'RESPONSABLE_CLUB', status: 'actif' },
    { nom: 'Bouzid', prenom: 'Leila', email: 'leila.bouzid@enicarthage.tn', role: 'ETUDIANT', status: 'inactif' },
  ];

  getRoleBadge(role: string): string {
    const badges: any = {
      'ETUDIANT': 'bg-blue-100 text-blue-700',
      'ENSEIGNANT': 'bg-green-100 text-green-700',
      'RESPONSABLE_CLUB': 'bg-purple-100 text-purple-700',
      'ADMIN': 'bg-red-100 text-red-700',
    };
    return badges[role] || 'bg-gray-100 text-gray-700';
  }
}