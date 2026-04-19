import { Component, OnInit } from '@angular/core';
import { ClubService } from '../../services/club.service';
import { Club, ClubApplication } from '../../../../core/models/club';

@Component({
  selector: 'app-clubs',
  templateUrl: './clubs.html',
  styleUrls: ['./clubs.scss'],
  standalone: false
})
export class ClubsComponent implements OnInit {
  clubs: Club[] = [];
  myApplications: ClubApplication[] = [];
  loading = true;

  constructor(private clubService: ClubService) {}

  ngOnInit(): void {
    this.loadClubs();
    this.loadMyApplications();
  }

  private loadClubs(): void {
    this.clubService.getActiveClubs().subscribe({
      next: (data) => this.clubs = data,
      error: (err) => console.error(err)
    });
  }

  private loadMyApplications(): void {
    this.clubService.getMyApplications().subscribe({
      next: (data) => this.myApplications = data,
      error: (err) => console.error(err)
    });
  }

  applyToClub(clubId: number, motivation: string): void {
    this.clubService.applyToClub(clubId, motivation).subscribe({
      next: (msg) => {
        alert(msg);
        this.loadMyApplications();
      },
      error: (err) => alert(err.error?.message || 'Erreur')
    });
  }
}