import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './sidebar.html'
})
export class SidebarComponent {
  navItems = [
    { label: 'Dashboard', icon: '📊', route: '/admin/dashboard' },
    { label: 'Utilisateurs', icon: '👥', route: '/admin/users' },
    { label: 'Événements', icon: '📅', route: '/admin/events' },
    { label: 'Réservations', icon: '🏫', route: '/admin/reservations' },
    { label: 'Clubs', icon: '🎯', route: '/admin/clubs' },
    { label: 'Demandes admin', icon: '📋', route: '/admin/requests' },
    { label: 'Notifications', icon: '🔔', route: '/admin/notifications' },
    { label: 'Analytics', icon: '📈', route: '/admin/analytics' },
    { label: 'Mon profil', icon: '👤', route: '/admin/profile' },
  ];
}
