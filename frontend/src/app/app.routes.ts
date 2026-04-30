import { Routes } from '@angular/router';

export const routes: Routes = [
    { path: '', redirectTo: 'etudiant/dashboard', pathMatch: 'full' },
    {
        path: 'auth',
        loadChildren: () =>
            import('./features/auth/auth-module').then(m => m.AuthModule)
    },
    // TEMPORARILY COMMENT OUT ADMIN
    // {
    //     path: 'admin',
    //     loadChildren: () =>
    //         import('./features/admin/admin-module').then(m => m.AdminModule)
    // },
    {
        path: 'enseignant',
        loadChildren: () => import('./features/enseignant/enseignant-module').then(m => m.EnseignantModule)
    },
    {
        path: 'etudiant',
        loadChildren: () => import('./features/etudiant/etudiant-module').then(m => m.EtudiantModule)
    },
    { path: '**', redirectTo: 'etudiant/dashboard' }
];