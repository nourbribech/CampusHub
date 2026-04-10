import { Component, OnInit, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { FullCalendarModule } from '@fullcalendar/angular';
import { CalendarOptions, EventInput, EventClickArg } from '@fullcalendar/core';
import dayGridPlugin from '@fullcalendar/daygrid';
import timeGridPlugin from '@fullcalendar/timegrid';
import interactionPlugin from '@fullcalendar/interaction';
import listPlugin from '@fullcalendar/list';
import frLocale from '@fullcalendar/core/locales/fr';

import { ReservationService, Reservation, Salle } from '../../services/reservation';
import { Header } from '../../../../shared/components/header/header';

@Component({
  selector: 'app-calendrier',
  standalone: true,
  imports: [CommonModule, FormsModule, FullCalendarModule, Header],
  templateUrl: './calendrier.html',
  styleUrls: ['./calendrier.scss']
})
export class Calendrier implements OnInit {
  calendarOptions: CalendarOptions = {
    plugins: [dayGridPlugin, timeGridPlugin, interactionPlugin, listPlugin],
    initialView: 'timeGridWeek',
    locale: frLocale,
    headerToolbar: {
      left: 'prev,next today',
      center: 'title',
      right: 'dayGridMonth,timeGridWeek,timeGridDay,listWeek'
    },
    buttonText: {
      today: "Aujourd'hui",
      month: 'Mois',
      week: 'Semaine',
      day: 'Jour',
      list: 'Liste'
    },
    slotMinTime: '07:00:00',
    slotMaxTime: '20:00:00',
    allDaySlot: false,
    height: 'auto',
    weekends: false, // Masquer le week-end par défaut
    events: [],
    eventClick: this.handleEventClick.bind(this),
    dateClick: this.handleDateClick.bind(this),
    eventDidMount: this.handleEventRender.bind(this)
  };

  reservations: Reservation[] = [];
  salles: Salle[] = [];
  loading = true;

  // Vue détails
  reservationSelectionnee?: Reservation;
  showDetails = false;

  // Filtres
  filtreBatiment = 'TOUS';
  filtreStatut = 'TOUS';

  // Statistiques
  nombreReservationsAujourdhui = 0;
  nombreConflits = 0;
  sallesDisponibles = 0;

  // Suggestions
  suggestions: CreneauSuggestion[] = [];
  showSuggestions = false;

  constructor(
    private reservationService: ReservationService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.chargerDonnees();
  }

  chargerDonnees(): void {
    this.loading = true;

    // Charger les réservations et les salles en parallèle
    Promise.all([
      this.reservationService.getMesReservations().toPromise(),
      this.reservationService.getSalles().toPromise()
    ]).then(([reservations, salles]) => {
      this.reservations = reservations || [];
      this.salles = salles || [];
      this.calculerStatistiques();
      this.afficherReservations();
      this.loading = false;
    }).catch(error => {
      console.error('❌ Erreur chargement données:', error);
      this.loading = false;
    });
  }

  afficherReservations(): void {
    let reservationsFiltrees = this.reservations;

    // Filtrer par bâtiment
    if (this.filtreBatiment !== 'TOUS') {
      reservationsFiltrees = reservationsFiltrees.filter(
        r => r.salle?.batiment === this.filtreBatiment
      );
    }

    // Filtrer par statut
    if (this.filtreStatut !== 'TOUS') {
      reservationsFiltrees = reservationsFiltrees.filter(
        r => r.statut === this.filtreStatut
      );
    }

    // Convertir en événements FullCalendar
    const events: EventInput[] = reservationsFiltrees.map(r => ({
      id: r.id,
      title: `${r.salle?.nom} - ${r.motif}`,
      start: r.dateDebut,
      end: r.dateFin,
      backgroundColor: this.getEventColor(r.statut),
      borderColor: this.getEventColor(r.statut),
      extendedProps: {
        reservation: r
      }
    }));

    this.calendarOptions.events = events;
  }

  getEventColor(statut: string): string {
    switch (statut) {
      case 'APPROUVEE': return '#10b981';
      case 'EN_ATTENTE': return '#f59e0b';
      case 'REJETEE': return '#ef4444';
      default: return '#6b7280';
    }
  }

  handleEventClick(arg: EventClickArg): void {
    const reservation = arg.event.extendedProps['reservation'] as Reservation;
    this.reservationSelectionnee = reservation;
    this.showDetails = true;
  }

  handleDateClick(arg: any): void {
    // Proposer de créer une réservation à cette date
    const dateSelectionnee = new Date(arg.date);
    this.proposerCreneaux(dateSelectionnee);
  }

  handleEventRender(arg: any): void {
    // Ajouter des tooltips
    arg.el.title = arg.event.extendedProps['reservation']?.motif || '';
  }

  calculerStatistiques(): void {
    const aujourdhui = new Date();
    aujourdhui.setHours(0, 0, 0, 0);
    const demain = new Date(aujourdhui);
    demain.setDate(demain.getDate() + 1);

    this.nombreReservationsAujourdhui = this.reservations.filter(r => {
      const dateDebut = new Date(r.dateDebut);
      return dateDebut >= aujourdhui && dateDebut < demain;
    }).length;

    // Détecter les conflits (même salle, même créneau)
    this.nombreConflits = this.detecterConflits();

    // Calculer salles disponibles maintenant
    this.sallesDisponibles = this.calculerSallesDisponibles();
  }

  detecterConflits(): number {
    let conflits = 0;
    const maintenant = new Date();

    for (let i = 0; i < this.reservations.length; i++) {
      const r1 = this.reservations[i];
      if (new Date(r1.dateDebut) < maintenant) continue;

      for (let j = i + 1; j < this.reservations.length; j++) {
        const r2 = this.reservations[j];

        if (r1.salleId === r2.salleId) {
          const debut1 = new Date(r1.dateDebut);
          const fin1 = new Date(r1.dateFin);
          const debut2 = new Date(r2.dateDebut);
          const fin2 = new Date(r2.dateFin);

          // Vérifier le chevauchement
          if (debut1 < fin2 && debut2 < fin1) {
            conflits++;
          }
        }
      }
    }

    return conflits;
  }

  calculerSallesDisponibles(): number {
    const maintenant = new Date();
    const sallesOccupees = new Set(
      this.reservations
        .filter(r => {
          const debut = new Date(r.dateDebut);
          const fin = new Date(r.dateFin);
          return debut <= maintenant && fin >= maintenant;
        })
        .map(r => r.salleId)
    );

    return this.salles.length - sallesOccupees.size;
  }

  proposerCreneaux(date: Date): void {
    this.suggestions = [];

    // Créneaux recommandés : 8h-10h, 10h-12h, 14h-16h, 16h-18h
    const creneaux = [
      { debut: 8, fin: 10 },
      { debut: 10, fin: 12 },
      { debut: 14, fin: 16 },
      { debut: 16, fin: 18 }
    ];

    for (const creneau of creneaux) {
      const dateDebut = new Date(date);
      dateDebut.setHours(creneau.debut, 0, 0, 0);

      const dateFin = new Date(date);
      dateFin.setHours(creneau.fin, 0, 0, 0);

      // Compter les salles disponibles pour ce créneau
      const sallesDisponibles = this.salles.filter(salle => {
        return !this.reservations.some(r => {
          if (r.salleId !== salle.id) return false;
          const rDebut = new Date(r.dateDebut);
          const rFin = new Date(r.dateFin);
          return rDebut < dateFin && dateDebut < rFin;
        });
      });

      if (sallesDisponibles.length > 0) {
        this.suggestions.push({
          dateDebut,
          dateFin,
          sallesDisponibles: sallesDisponibles.length,
          salleSuggestion: sallesDisponibles[0]
        });
      }
    }

    this.showSuggestions = this.suggestions.length > 0;
  }

  appliquerFiltreBatiment(batiment: string): void {
    this.filtreBatiment = batiment;
    this.afficherReservations();
  }

  appliquerFiltreStatut(statut: string): void {
    this.filtreStatut = statut;
    this.afficherReservations();
  }

  fermerDetails(): void {
    this.showDetails = false;
    this.reservationSelectionnee = undefined;
  }

  fermerSuggestions(): void {
    this.showSuggestions = false;
    this.suggestions = [];
  }

  reserverCreneau(suggestion: CreneauSuggestion): void {
    // Rediriger vers la page de réservation avec les données pré-remplies
    this.router.navigate(['/enseignant/reservation-salle'], {
      state: {
        sallePreselection: suggestion.salleSuggestion,
        dateDebut: suggestion.dateDebut,
        dateFin: suggestion.dateFin
      }
    });
  }

  exporterCalendrier(): void {
    // Générer un fichier iCal
    let ical = 'BEGIN:VCALENDAR\n';
    ical += 'VERSION:2.0\n';
    ical += 'PRODID:-//CampusHub ENICarthage//FR\n';
    ical += 'CALSCALE:GREGORIAN\n';

    this.reservations.forEach(r => {
      ical += 'BEGIN:VEVENT\n';
      ical += `UID:${r.id}@campushub.enicarthage.tn\n`;
      ical += `DTSTAMP:${this.formatDateICal(new Date())}\n`;
      ical += `DTSTART:${this.formatDateICal(new Date(r.dateDebut))}\n`;
      ical += `DTEND:${this.formatDateICal(new Date(r.dateFin))}\n`;
      ical += `SUMMARY:${r.salle?.nom} - ${r.motif}\n`;
      ical += `LOCATION:${r.salle?.batiment} - Étage ${r.salle?.etage}\n`;
      ical += `DESCRIPTION:${r.motif}\\nCapacité: ${r.salle?.capacite}\\nParticipants: ${r.nombreParticipants || 'N/A'}\n`;
      ical += `STATUS:${r.statut === 'APPROUVEE' ? 'CONFIRMED' : 'TENTATIVE'}\n`;
      ical += 'END:VEVENT\n';
    });

    ical += 'END:VCALENDAR';

    // Télécharger le fichier
    const blob = new Blob([ical], { type: 'text/calendar' });
    const url = window.URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = 'calendrier-campushub.ics';
    a.click();
    window.URL.revokeObjectURL(url);

    alert('✅ Calendrier exporté ! Vous pouvez l\'importer dans Google Calendar, Outlook, etc.');
  }

  formatDateICal(date: Date): string {
    return date.toISOString().replace(/[-:]/g, '').split('.')[0] + 'Z';
  }

  annulerReservation(id: string): void {
    if (confirm('Êtes-vous sûr de vouloir annuler cette réservation ?')) {
      this.reservationService.annulerReservation(id).subscribe({
        next: () => {
          alert('✅ Réservation annulée avec succès');
          this.fermerDetails();
          this.chargerDonnees();
        },
        error: (error) => {
          console.error('❌ Erreur annulation:', error);
          alert('❌ Erreur lors de l\'annulation');
        }
      });
    }
  }
}

interface CreneauSuggestion {
  dateDebut: Date;
  dateFin: Date;
  sallesDisponibles: number;
  salleSuggestion: Salle;
}