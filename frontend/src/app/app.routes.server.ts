import { RenderMode, ServerRoute } from '@angular/ssr';

export const serverRoutes: ServerRoute[] = [
  {
    path: '',
    renderMode: RenderMode.Server
  },
  {
    path: 'home',
    renderMode: RenderMode.Server
  },
  {
    path: 'about',
    renderMode: RenderMode.Server
  },
  {
    path: 'programmazione',
    renderMode: RenderMode.Server
  },
  {
    path: 'film',
    renderMode: RenderMode.Server
  },
  {
    path: 'sale',
    renderMode: RenderMode.Server
  },
  {
    path: 'admin/**',
    renderMode: RenderMode.Client
  },
  {
    path: 'programmazione/:programmazioneId/sala',
    renderMode: RenderMode.Server
  },
  {
    path: 'registrati',
    renderMode: RenderMode.Server
  },
  {
    path: 'ticket-show/:ordineId',
    renderMode: RenderMode.Server
  },
  {
    path: 'ordini',
    renderMode: RenderMode.Server
  },
  {
    path: '**',
    renderMode: RenderMode.Server
  }
];
