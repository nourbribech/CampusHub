import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ReservationService, Reservation } from '../../services/reservation';
import { Header } from '../../../../shared/components/header/header';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, RouterModule, Header],
  templateUrl: './dashboard.html',
  styleUrls: ['./dashboard.scss']
})
export class DashboardComponent implements OnInit {
  reservationsAVenir: Reservation[] = [];
  prochaineReservation?: Reservation;
  nombreReservationsActives = 0;
  loading = true;
  userName = 'Jean Dupont';
  userRole = 'Enseignant - Département Informatique';

  constructor(private reservationService: ReservationService) { }

  ngOnInit(): void {
    this.chargerReservations();
  }

  chargerReservations(): void {
    this.loading = true;
    this.reservationService.getMesReservations().subscribe({
      next: (reservations) => {
        const maintenant = new Date();

        // Filtrer et trier les réservations à venir
        this.reservationsAVenir = reservations
          .filter(r => {
            const dateDebut = new Date(r.dateDebut);
            return dateDebut > maintenant && r.statut === 'APPROUVEE';
          })
          .sort((a, b) => {
            return new Date(a.dateDebut).getTime() - new Date(b.dateDebut).getTime();
          });

        this.prochaineReservation = this.reservationsAVenir[0];
        this.nombreReservationsActives = this.reservationsAVenir.length;
        this.loading = false;

        console.log('✅ Réservations chargées:', reservations);
      },
      error: (error) => {
        console.error('❌ Erreur lors du chargement des réservations', error);
        this.loading = false;
      }
    });
  }

  annulerReservation(id: string): void {
    if (confirm('Êtes-vous sûr de vouloir annuler cette réservation ?')) {
      this.reservationService.annulerReservation(id).subscribe({
        next: () => {
          this.chargerReservations();
          alert('✅ Réservation annulée avec succès');
        },
        error: (error) => {
          console.error('❌ Erreur lors de l\'annulation', error);
          alert('❌ Erreur lors de l\'annulation de la réservation');
        }
      });
    }
  }

  getStatutClass(statut: string): string {
    switch (statut) {
      case 'APPROUVEE': return 'bg-green-100 text-green-700';
      case 'EN_ATTENTE': return 'bg-yellow-100 text-yellow-700';
      case 'REJETEE': return 'bg-red-100 text-red-700';
      default: return 'bg-gray-100 text-gray-700';
    }
  }
}