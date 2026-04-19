import { Component, OnInit } from '@angular/core';
import { DemandeService } from '../../services/demande.service';
import { Demande } from '../../../../core/models/demande';

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
      next: (data) => {
        this.demandes = data;
        this.loading = false;
      },
      error: () => this.loading = false
    });
  }
}