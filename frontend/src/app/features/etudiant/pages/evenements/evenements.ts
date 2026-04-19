import { Component, OnInit } from '@angular/core';
import { EvenementService } from '../../services/evenement.service';
import { Evenement } from '../../../../core/models/evenement';

@Component({
  selector: 'app-evenements',
  templateUrl: './evenements.html',
  styleUrls: ['./evenements.scss'],
  standalone: false
})
export class EvenementsComponent implements OnInit {
  evenements: Evenement[] = [];
  myRegistrations: Evenement[] = [];
  loading = true;

  constructor(private evenementService: EvenementService) {}

  ngOnInit(): void {
    this.loadEvenements();
    this.loadMyRegistrations();
  }

  private loadEvenements(): void {
    this.evenementService.getApprovedEvents().subscribe({
      next: (data) => this.evenements = data,
      error: (err) => console.error(err)
    });
  }

  private loadMyRegistrations(): void {
    this.evenementService.getMyRegistrations().subscribe({
      next: (data) => this.myRegistrations = data,
      error: (err) => console.error(err)
    });
  }

  register(eventId: number): void {
    this.evenementService.registerForEvent(eventId).subscribe({
      next: (msg) => {
        alert(msg);
        this.loadMyRegistrations();
      },
      error: (err) => alert(err.error?.message || 'Erreur')
    });
  }
}