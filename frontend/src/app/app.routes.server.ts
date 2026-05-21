import { RenderMode, ServerRoute } from '@angular/ssr';

export const serverRoutes: ServerRoute[] = [
  {
    path: 'admin/**',
    renderMode: RenderMode.Client
  },
  {
    path: 'programmazione/:programmazioneId/sala',
    renderMode: RenderMode.Server
  },
  {
    path: 'ticket-show/:ordineId',
    renderMode: RenderMode.Server
  },
  {
    path: '**',
    renderMode: RenderMode.Prerender
  }
];
