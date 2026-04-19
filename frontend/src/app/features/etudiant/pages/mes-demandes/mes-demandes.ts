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
  countByStatus(status: string): number {
  return this.demandes.filter(d => d.status === status).length;
}
getStatutClass(status: string): string {
  const map: Record<string, string> = {
    APPROVED: 'bg-green-100 text-green-700 border-green-200',
    REJECTED: 'bg-red-100 text-red-700 border-red-200',
    PENDING: 'bg-yellow-100 text-yellow-700 border-yellow-200'
  };
  return map[status] || '';
}
getStatutIcon(status: string): string {
  return { APPROVED: '✅', REJECTED: '❌', PENDING: '⏳' }[status] || '';
}
getStatutLabel(status: string): string {
  return { APPROVED: 'Approuvée', REJECTED: 'Rejetée', PENDING: 'En attente' }[status] || status;
}
ouvrirNouvelleDemandeModal(): void { /* à implémenter */ }
}