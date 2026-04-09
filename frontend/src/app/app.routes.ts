import { Routes } from '@angular/router';

export const routes: Routes = [
    {
        path: '',
        redirectTo: 'enseignant/dashboard',
        pathMatch: 'full'
    },
    {
        path: 'enseignant',
        loadChildren: () => import('./features/enseignant/enseignant-routing-module')
            .then(m => m.EnseignantRoutingModule)
    },
    {
        path: '**',
        redirectTo: 'enseignant/dashboard'
    }
];