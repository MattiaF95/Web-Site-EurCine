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
