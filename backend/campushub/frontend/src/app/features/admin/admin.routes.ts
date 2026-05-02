import { Routes } from '@angular/router';

export const ADMIN_ROUTES: Routes = [
    // TODO: create your admin components here
    // { path: 'dashboard', component: AdminDashboardComponent },
    { path: '', redirectTo: 'dashboard', pathMatch: 'full' }
];
