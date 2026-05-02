import { Routes } from '@angular/router';
import { AuthGuard } from './core/guards/auth-guard';

export const routes: Routes = [
    { path: '', redirectTo: 'auth/login', pathMatch: 'full' },
    {
        path: 'auth',
        loadChildren: () =>
            import('./features/auth/auth-module').then(m => m.AuthModule)
    },
    {
        path: 'enseignant',
        canActivate: [AuthGuard],
        loadChildren: () => import('./features/enseignant/enseignant-module').then(m => m.EnseignantModule)
    },
    {
        path: 'etudiant',
        canActivate: [AuthGuard],
        loadChildren: () => import('./features/etudiant/etudiant-module').then(m => m.EtudiantModule)
    },
    { path: '**', redirectTo: 'auth/login' }
];