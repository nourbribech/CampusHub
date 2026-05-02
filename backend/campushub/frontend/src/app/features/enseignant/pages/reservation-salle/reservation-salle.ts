import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { ReservationService, Salle, CreateReservationDto } from '../../services/reservation';
import { Header } from '../../../../shared/components/header/header';

@Component({
  selector: 'app-reservation-salle',
  standalone: true,
  imports: [CommonModule, FormsModule, Header],
  templateUrl: './reservation-salle.html',
  styleUrls: ['./reservation-salle.scss']
})
export class ReservationSalle implements OnInit {
  // Étape du formulaire
  etapeActuelle = 1;

  // Données du formulaire
  salles: Salle[] = [];
  sallesFiltrees: Salle[] = [];
  etagesDisponibles: number[] = [];
  filtreBatiment: string = 'TOUS';
  filtreEtage: string | number = 'TOUS';
  salleSelectionnee?: Salle;
  dateDebut = '';
  heureDebut = '';
  dateFin = '';
  heureFin = '';
  motif = '';
  nombreParticipants: number | undefined;
  // États
  loading = false;
  loadingSalles = true;
  error = '';

  // Date minimale (aujourd'hui)
  dateMin = '';

  constructor(
    private reservationService: ReservationService,
    private router: Router
  ) {
    // Définir la date minimale à aujourd'hui
    const today = new Date();
    this.dateMin = today.toISOString().split('T')[0];
  }

  ngOnInit(): void {
    this.chargerSalles();
  }

  chargerSalles(): void {
    this.loadingSalles = true;
    this.reservationService.getSalles().subscribe({
      next: (salles) => {
        this.salles = salles;
        this.sallesFiltrees = salles;
        this.etagesDisponibles = [...new Set(salles.map(s => s.etage))].sort((a, b) => a - b);
        this.loadingSalles = false;
        console.log('✅ Salles chargées:', salles.length, 'salles');
      },
      error: (error) => {
        console.error('❌ Erreur chargement salles:', error);
        this.loadingSalles = false;
        this.error = 'Erreur lors du chargement des salles';
      }
    });
  }

  filtrerParBatiment(batiment: string): void {
    this.filtreBatiment = batiment;
    this.appliquerFiltres();
  }

  filtrerParEtage(etage: string | number): void {
    this.filtreEtage = etage;
    this.appliquerFiltres();
  }

  appliquerFiltres(): void {
    this.sallesFiltrees = this.salles.filter(s => {
      const matchBatiment = this.filtreBatiment === 'TOUS' || s.batiment === this.filtreBatiment;
      const matchEtage = this.filtreEtage === 'TOUS' || s.etage === this.filtreEtage;
      return matchBatiment && matchEtage;
    });
  }

  selectionnerSalle(salle: Salle): void {
    this.salleSelectionnee = salle;
    this.etapeActuelle = 2;
  }

  retourEtape(etape: number): void {
    this.etapeActuelle = etape;
    this.error = '';
  }

  validerEtape2(): void {
    // Vérifier que tous les champs sont remplis
    if (!this.dateDebut || !this.heureDebut || !this.dateFin || !this.heureFin) {
      this.error = 'Veuillez remplir tous les champs de date et heure';
      return;
    }

    // Construire les dates complètes
    const debut = new Date(`${this.dateDebut}T${this.heureDebut}`);
    const fin = new Date(`${this.dateFin}T${this.heureFin}`);

    // Vérifier que la date de fin est après la date de début
    if (fin <= debut) {
      this.error = 'La date de fin doit être après la date de début';
      return;
    }

    // Vérifier que la durée n'est pas trop longue (max 8 heures)
    const dureeHeures = (fin.getTime() - debut.getTime()) / (1000 * 60 * 60);
    if (dureeHeures > 8) {
      this.error = 'La durée maximale d\'une réservation est de 8 heures';
      return;
    }

    this.error = '';
    this.etapeActuelle = 3;
  }

  soumettre(): void {
    if (!this.motif.trim()) {
      this.error = 'Veuillez indiquer le motif de la réservation';
      return;
    }

    if (this.nombreParticipants != null && this.salleSelectionnee) {
      if (this.nombreParticipants > this.salleSelectionnee.capacite) {
        this.error = `Le nombre de participants ne peut pas dépasser la capacité de la salle (${this.salleSelectionnee.capacite})`;
        return;
      }
    }

    this.loading = true;
    this.error = '';

    const reservation: CreateReservationDto = {
      salleId: this.salleSelectionnee!.id,
      dateDebut: `${this.dateDebut}T${this.heureDebut}:00`,
      dateFin: `${this.dateFin}T${this.heureFin}:00`,
      motif: this.motif,
      nombreParticipants: this.nombreParticipants
    };

    this.reservationService.creerReservation(reservation).subscribe({
      next: (result) => {
        console.log('✅ Réservation créée:', result);
        alert('✅ Réservation créée avec succès ! Elle est en attente d\'approbation.');
        this.router.navigate(['/enseignant/mes-reservations']);
      },
      error: (error) => {
        console.error('❌ Erreur création réservation:', error);
        this.loading = false;
        this.error = 'Erreur lors de la création de la réservation. Veuillez réessayer.';
      }
    });
  }

  annuler(): void {
    if (confirm('Êtes-vous sûr de vouloir annuler ?')) {
      this.router.navigate(['/enseignant/dashboard']);
    }
  }
}
// Fin du composant ReservationSalle