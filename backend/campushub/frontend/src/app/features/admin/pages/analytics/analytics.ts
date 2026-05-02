import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-analytics',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './analytics.html'
})
export class Analytics {
  stats = [
    { label: 'Total utilisateurs', value: '1,248', change: '+12%', up: true, icon: '👥' },
    { label: 'Événements ce mois', value: '34', change: '+8%', up: true, icon: '📅' },
    { label: 'Taux de participation', value: '78%', change: '+5%', up: true, icon: '🎯' },
    { label: 'Demandes traitées', value: '156', change: '-3%', up: false, icon: '📋' },
  ];

  eventsByMonth = [
    { month: 'Jan', count: 8, height: '40%' },
    { month: 'Fév', count: 12, height: '60%' },
    { month: 'Mar', count: 18, height: '90%' },
    { month: 'Avr', count: 15, height: '75%' },
    { month: 'Mai', count: 20, height: '100%' },
    { month: 'Jun', count: 14, height: '70%' },
  ];

  usersByRole = [
    { role: 'Étudiants', count: 980, percentage: 78, color: 'bg-blue-500' },
    { role: 'Enseignants', count: 145, percentage: 12, color: 'bg-green-500' },
    { role: 'Clubs', count: 98, percentage: 8, color: 'bg-purple-500' },
    { role: 'Admins', count: 25, percentage: 2, color: 'bg-red-500' },
  ];

  recentActivity = [
    { action: 'Nouvel événement créé', detail: 'Hackathon IA — Club Robotique', time: 'Il y a 2h', icon: '📅' },
    { action: 'Réservation approuvée', detail: 'Amphi A — Dr. Trabelsi', time: 'Il y a 3h', icon: '🏫' },
    { action: 'Nouveau membre inscrit', detail: 'Ahmed Ben Ali — Étudiant', time: 'Il y a 5h', icon: '👤' },
    { action: 'Demande traitée', detail: 'Certificat de scolarité — Leila Bouzid', time: 'Il y a 6h', icon: '📋' },
    { action: 'Club approuvé', detail: 'Club Intelligence Artificielle', time: 'Il y a 1j', icon: '🎯' },
  ];
}
