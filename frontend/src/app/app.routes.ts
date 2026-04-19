import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: '',
    redirectTo: 'etudiant',       
    pathMatch: 'full'
  },

 
  {
    path: 'enseignant',
    loadChildren: () => import('./features/enseignant/enseignant-routing-module')
      .then(m => m.EnseignantRoutingModule)
  },

  
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