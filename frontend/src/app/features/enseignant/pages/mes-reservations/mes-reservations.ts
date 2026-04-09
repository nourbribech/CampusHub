import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { ReservationService, Reservation } from '../../services/reservation';

@Component({
  selector: 'app-mes-reservations',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './mes-reservations.html',
  styleUrls: ['./mes-reservations.scss']
})
export class MesReservations implements OnInit {
  reservations: Reservation[] = [];
  reservationsFiltrees: Reservation[] = [];
  loading = true;

  // Filtres
  filtreStatut: string = 'TOUTES';
  filtrePeriode: string = 'TOUTES';
  rechercheTexte: string = '';

  // Statistiques
  nombreTotal = 0;
  nombreApprouvees = 0;
  nombreEnAttente = 0;
  nombreRejetees = 0;

  triOrdre: 'DESC' | 'ASC' = 'DESC';

  toggleTri(): void {
    this.triOrdre = this.triOrdre === 'DESC' ? 'ASC' : 'DESC';
    this.reservationsFiltrees.sort((a, b) => {
      const dateA = new Date(a.dateDebut).getTime();
      const dateB = new Date(b.dateDebut).getTime();
      return this.triOrdre === 'DESC' ? dateB - dateA : dateA - dateB;
    });
  }

  constructor(private reservationService: ReservationService) { }

  ngOnInit(): void {
    this.chargerReservations();
  }

  chargerReservations(): void {
    this.loading = true;
    this.reservationService.getMesReservations().subscribe({
      next: (reservations) => {
        this.reservations = reservations.sort((a, b) => {
          return new Date(b.dateDebut).getTime() - new Date(a.dateDebut).getTime();
        });
        this.calculerStatistiques();
        this.appliquerFiltres();
        this.loading = false;
        console.log('✅ Réservations chargées:', reservations);
      },
      error: (error) => {
        console.error('❌ Erreur chargement réservations:', error);
        this.loading = false;
      }
    });
  }

  calculerStatistiques(): void {
    this.nombreTotal = this.reservations.length;
    this.nombreApprouvees = this.reservations.filter(r => r.statut === 'APPROUVEE').length;
    this.nombreEnAttente = this.reservations.filter(r => r.statut === 'EN_ATTENTE').length;
    this.nombreRejetees = this.reservations.filter(r => r.statut === 'REJETEE').length;
  }

  appliquerFiltres(): void {
    let resultats = [...this.reservations];

    // Filtre par statut
    if (this.filtreStatut !== 'TOUTES') {
      resultats = resultats.filter(r => r.statut === this.filtreStatut);
    }

    // Filtre par période
    const maintenant = new Date();
    if (this.filtrePeriode === 'A_VENIR') {
      resultats = resultats.filter(r => new Date(r.dateDebut) > maintenant);
    } else if (this.filtrePeriode === 'PASSEES') {
      resultats = resultats.filter(r => new Date(r.dateDebut) <= maintenant);
    }

    // Recherche textuelle
    if (this.rechercheTexte.trim()) {
      const recherche = this.rechercheTexte.toLowerCase();
      resultats = resultats.filter(r =>
        r.salle?.nom.toLowerCase().includes(recherche) ||
        r.motif.toLowerCase().includes(recherche) ||
        r.salle?.batiment.toLowerCase().includes(recherche)
      );
    }

    this.reservationsFiltrees = resultats;
  }

  changerFiltreStatut(statut: string): void {
    this.filtreStatut = statut;
    this.appliquerFiltres();
  }

  changerFiltrePeriode(periode: string): void {
    this.filtrePeriode = periode;
    this.appliquerFiltres();
  }

  onRechercheChange(): void {
    this.appliquerFiltres();
  }

  annulerReservation(id: string): void {
    if (confirm('Êtes-vous sûr de vouloir annuler cette réservation ?')) {
      this.reservationService.annulerReservation(id).subscribe({
        next: () => {
          alert('✅ Réservation annulée avec succès');
          this.chargerReservations();
        },
        error: (error) => {
          console.error('❌ Erreur annulation:', error);
          alert('❌ Erreur lors de l\'annulation de la réservation');
        }
      });
    }
  }

  getStatutClass(statut: string): string {
    switch (statut) {
      case 'APPROUVEE': return 'bg-green-100 text-green-700 border-green-200';
      case 'EN_ATTENTE': return 'bg-yellow-100 text-yellow-700 border-yellow-200';
      case 'REJETEE': return 'bg-red-100 text-red-700 border-red-200';
      default: return 'bg-gray-100 text-gray-700 border-gray-200';
    }
  }

  getStatutIcon(statut: string): string {
    switch (statut) {
      case 'APPROUVEE': return '✅';
      case 'EN_ATTENTE': return '⏳';
      case 'REJETEE': return '❌';
      default: return '❓';
    }
  }

  isPasse(reservation: Reservation): boolean {
    return new Date(reservation.dateDebut) < new Date();
  }
}