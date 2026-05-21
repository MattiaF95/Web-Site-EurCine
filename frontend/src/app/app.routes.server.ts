import { RenderMode, ServerRoute } from '@angular/ssr';

export const serverRoutes: ServerRoute[] = [
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
