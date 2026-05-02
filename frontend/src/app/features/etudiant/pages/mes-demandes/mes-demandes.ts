import { Component, OnInit } from '@angular/core';
import { DemandeService } from '../../services/demande.service';
import { Demande } from '../../../../core/models';

@Component({
  selector: 'app-mes-demandes',
  templateUrl: './mes-demandes.html',
  styleUrls: ['./mes-demandes.scss'],
  standalone: false
})
export class MesDemandesComponent implements OnInit {
  demandes: Demande[] = [];
  loading = true;

  constructor(private demandeService: DemandeService) {}

  ngOnInit(): void {
    this.demandeService.getMyRequests().subscribe({
      next: (data) => this.demandes = data,
      error: (err) => console.error(err),
      complete: () => this.loading = false
    });
  }
  countByStatus(statut: string): number {
  return this.demandes.filter(d => d.statut === statut).length;
}
getStatutClass(statut: string): string {
  const map: Record<string, string> = {
    'APPROUVE':   'bg-green-100 text-green-700 border-green-200',
    'REJETE':     'bg-red-100 text-red-700 border-red-200',
    'EN_ATTENTE': 'bg-yellow-100 text-yellow-700 border-yellow-200'
  };
  return  map[statut] ?? '';
}
getStatutIcon(statut: string): string {
  return { 'APPROUVE': '✅', 'REJETE': '❌', 'EN_ATTENTE': '⏳' }[statut] || '';
}
getStatutLabel(statut: string): string {
  return { 'APPROUVE': 'Approuvée', 'REJETE': 'Rejetée', 'EN_ATTENTE': 'En attente' }[statut] || status;
}
ouvrirNouvelleDemandeModal(): void {
  // à implémenter
}
}