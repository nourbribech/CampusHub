import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-requests',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './requests.html'
})
export class Requests {
  activeTab = 'tous';
  rejectComment = '';
  showRejectModal = false;
  selectedRequest: any = null;

  requests = [
    { id: 1, type: 'CERTIFICAT', demandeur: 'Ahmed Ben Ali', detail: 'Certificat de scolarité', date: '18 Avr 2025', status: 'EN_ATTENTE' },
    { id: 2, type: 'REMBOURSEMENT', demandeur: 'Club Robotique', detail: 'Remboursement matériel 250 TND', date: '17 Avr 2025', status: 'EN_ATTENTE' },
    { id: 3, type: 'CERTIFICAT', demandeur: 'Leila Bouzid', detail: 'Attestation de réussite', date: '16 Avr 2025', status: 'APPROUVE' },
    { id: 4, type: 'MATERIEL', demandeur: 'Club Dev', detail: 'Demande projecteur + écran', date: '15 Avr 2025', status: 'REJETE' },
    { id: 5, type: 'REMBOURSEMENT', demandeur: 'Sonia Trabelsi', detail: 'Remboursement déplacement 80 TND', date: '14 Avr 2025', status: 'EN_ATTENTE' },
  ];

  get filteredRequests() {
    if (this.activeTab === 'tous') return this.requests;
    return this.requests.filter(r => r.status === this.activeTab.toUpperCase());
  }

  approuver(r: any): void { r.status = 'APPROUVE'; }

  openRejectModal(r: any): void {
    this.selectedRequest = r;
    this.showRejectModal = true;
    this.rejectComment = '';
  }

  confirmerRejet(): void {
    if (this.selectedRequest) {
      this.selectedRequest.status = 'REJETE';
      this.selectedRequest.comment = this.rejectComment;
    }
    this.showRejectModal = false;
    this.selectedRequest = null;
  }

  getTypeIcon(type: string): string {
    const icons: any = { 'CERTIFICAT': '📜', 'REMBOURSEMENT': '💰', 'MATERIEL': '🖥️' };
    return icons[type] || '📋';
  }

  getStatusClass(status: string): string {
    const classes: any = {
      'EN_ATTENTE': 'bg-yellow-100 text-yellow-700',
      'APPROUVE': 'bg-green-100 text-green-700',
      'REJETE': 'bg-red-100 text-red-700',
    };
    return classes[status] || 'bg-gray-100 text-gray-700';
  }
}