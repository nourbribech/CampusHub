import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AdminShellComponent } from './layout/admin-shell/admin-shell';
import { Dashboard } from './pages/dashboard/dashboard';
import { Users } from './pages/users/users';
import { EventsValidation } from './pages/events-validation/events-validation';
import { Reservations } from './pages/reservations/reservations';
import { Clubs } from './pages/clubs/clubs';
import { Requests } from './pages/requests/requests';
import { Notifications } from './pages/notifications/notifications';
import { Analytics } from './pages/analytics/analytics';
import { Profile } from './pages/profile/profile';

const routes: Routes = [
    {
        path: '',
        component: AdminShellComponent,
        children: [
            { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
            { path: 'dashboard', component: Dashboard },
            { path: 'users', component: Users },
            { path: 'events', component: EventsValidation },
            { path: 'reservations', component: Reservations },
            { path: 'clubs', component: Clubs },
            { path: 'requests', component: Requests },
            { path: 'notifications', component: Notifications },
            { path: 'analytics', component: Analytics },
            { path: 'profile', component: Profile },
        ]
    }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class AdminRoutingModule { }