import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-notifications',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './notifications.html'
})
export class Notifications {
  message = '';
  titre = '';
  cible = 'TOUS';
  sent = false;
  loading = false;

  historique = [
    { titre: 'Rappel inscription', message: 'N\'oubliez pas de vous inscrire aux événements', cible: 'ETUDIANTS', date: '17 Avr 2025', lu: 245 },
    { titre: 'Maintenance plateforme', message: 'La plateforme sera en maintenance ce samedi', cible: 'TOUS', date: '15 Avr 2025', lu: 512 },
    { titre: 'Nouveaux clubs disponibles', message: 'De nouveaux clubs sont ouverts aux inscriptions', cible: 'ETUDIANTS', date: '10 Avr 2025', lu: 380 },
  ];

  envoyer(): void {
    if (!this.titre || !this.message) return;
    this.loading = true;
    setTimeout(() => {
      this.loading = false;
      this.sent = true;
      this.historique.unshift({
        titre: this.titre,
        message: this.message,
        cible: this.cible,
        date: 'Aujourd\'hui',
        lu: 0
      });
      this.titre = '';
      this.message = '';
      setTimeout(() => this.sent = false, 3000);
    }, 1500);
  }
}
