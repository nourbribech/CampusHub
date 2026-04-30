import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { SidebarComponent } from '../sidebar/sidebar';
import { AuthService } from '../../../../core/services/auth';

@Component({
  selector: 'app-admin-shell',
  standalone: true,
  imports: [CommonModule, RouterModule, SidebarComponent],
  templateUrl: './admin-shell.html'
})
export class AdminShellComponent {
  private authService = inject(AuthService);
  currentUser = this.authService.currentUser;

  logout(): void {
    this.authService.logout();
  }
}