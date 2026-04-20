import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './profile.html'
})
export class Profile {
  saved = false;

  admin = {
    prenom: 'Mohamed',
    nom: 'Admin',
    email: 'admin@enicarthage.tn',
    telephone: '+216 71 000 000',
    role: 'Administrateur',
    depuis: 'Septembre 2023'
  };

  passwords = {
    current: '',
    new: '',
    confirm: ''
  };

  saveProfile(): void {
    this.saved = true;
    setTimeout(() => this.saved = false, 3000);
  }

  changePassword(): void {
    if (this.passwords.new !== this.passwords.confirm) return;
    this.passwords = { current: '', new: '', confirm: '' };
  }
}