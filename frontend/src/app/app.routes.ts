import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: '',
    redirectTo: 'etudiant',           // ← changed to module root (no /dashboard)
    pathMatch: 'full'
  },

  // Enseignant module (your friend's work - unchanged)
  {
    path: 'enseignant',
    loadChildren: () => import('./features/enseignant/enseignant-routing-module')
      .then(m => m.EnseignantRoutingModule)
  },

  // Etudiant module (your work)
  {
    path: 'etudiant',
    loadChildren: () => import('./features/etudiant/etudiant-routing-module')
      .then(m => m.EtudiantRoutingModule)
  },

  // Catch-all
  {
    path: '**',
    redirectTo: 'etudiant'
  }
];