import { Routes } from '@angular/router';
import { adminAuthGuard } from './core/guard/admin-auth.guard';
import { userAuthGuard } from './core/guard/user-auth.guard';

export const routes: Routes = [
  {
    path: '',
    pathMatch: 'full',
    redirectTo: 'home'
  },
  {
    path: 'home',
    loadComponent: () => import('./pages/home/home.component').then((m) => m.HomeComponent)
  },
  {
    path: 'about',
    loadComponent: () => import('./pages/about/about.component').then((m) => m.AboutComponent)
  },
  {
    path: 'programmazione',
    loadComponent: () =>
      import('./pages/programmazione/programmazione.component').then((m) => m.ProgrammazioneComponent)
  },
  {
    path: 'programmazione/:programmazioneId/sala',
    loadComponent: () => import('./pages/sala-cinema/sala-cinema.component').then((m) => m.SalaCinemaComponent)
  },
  {
    path: 'registrati',
    loadComponent: () => import('./pages/register/register.component').then((m) => m.RegisterComponent)
  },
  {
    path: 'ticket-show/:numeroOrdine',
    loadComponent: () => import('./pages/ticket-show/ticket-show.component').then((m) => m.TicketShowComponent)
  },
  {
    path: 'ordini',
    canActivate: [userAuthGuard],
    loadComponent: () => import('./pages/ordini/ordini.component').then((m) => m.OrdiniComponent)
  },
  {
    path: 'film',
    loadComponent: () => import('./pages/film/film.component').then((m) => m.FilmComponent)
  },
  {
    path: 'admin/film',
    canActivate: [adminAuthGuard],
    loadComponent: () =>
      import('./pages/admin-film-management/admin-film-management.component').then((m) => m.AdminFilmManagementComponent)
  },
  {
    path: 'admin/film/aggiungi',
    canActivate: [adminAuthGuard],
    loadComponent: () => import('./pages/admin-film-create/admin-film-create.component').then((m) => m.AdminFilmCreateComponent)
  },
  {
    path: 'admin/film/modifica',
    canActivate: [adminAuthGuard],
    loadComponent: () => import('./pages/admin-film-edit/admin-film-edit.component').then((m) => m.AdminFilmEditComponent)
  },
  {
    path: 'admin/film/elimina',
    canActivate: [adminAuthGuard],
    loadComponent: () => import('./pages/admin-film-delete/admin-film-delete.component').then((m) => m.AdminFilmDeleteComponent)
  },
  {
    path: 'admin/programmazione',
    canActivate: [adminAuthGuard],
    loadComponent: () =>
      import('./pages/admin-programmazione-management/admin-programmazione-management.component').then(
        (m) => m.AdminProgrammazioneManagementComponent
      )
  },
  {
    path: 'sale',
    loadComponent: () => import('./pages/sale/sale.component').then((m) => m.SaleComponent)
  },
  {
    path: '**',
    redirectTo: 'home'
  }
];
