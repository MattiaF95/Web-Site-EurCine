import { Routes } from '@angular/router';

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
    path: 'ticket-show/:ordineId',
    loadComponent: () => import('./pages/ticket-show/ticket-show.component').then((m) => m.TicketShowComponent)
  },
  {
    path: 'film',
    loadComponent: () => import('./pages/film/film.component').then((m) => m.FilmComponent)
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
