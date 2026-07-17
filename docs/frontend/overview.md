# Frontend Angular

## Scopo

Il frontend è un'app Angular 21.2 con SSR tramite Express 5. Le pagine sono lazy-loaded e usano servizi core per API, autenticazione, cache, ordini, catalogo, programmazione e gestione admin.

## Routing

Le route pubbliche coprono home, film, programmazione, sale e informazioni. La selezione posti usa `/programmazione/:programmazioneId/sala`; gli ordini usano `/ordini`; le pagine admin sono sotto `/admin/film/**` e `/admin/programmazione`.

`userAuthGuard` protegge l'area ordini e `adminAuthGuard` protegge le pagine amministrative.

## Sandbox di sessione

`SessionSandboxService` mantiene in `sessionStorage` utenti mock, token, ordini, variazioni di film/programmazione e cache HTTP. La sandbox è legata alla sessione del browser, non sostituisce il database e viene persa alla chiusura della tab o del browser.

In locale il client usa `http://localhost:8080`; il build production usa l'URL configurato in `src/environments/environment.prod.ts`. Gli interceptor gestiscono token, CSRF e cache; i servizi decidono quando usare API reali o stato mock in base al contesto di produzione.

## Sviluppo

```bash
cd frontend
npm install
npm start
```

Per validare il build usare `npm run build`; i test usano Vitest tramite `npm test`.

## Fonti

- `frontend/src/app/app.routes.ts`
- `frontend/src/app/core/service/session-sandbox.service.ts`
- `frontend/src/app/core/guard/user-auth.guard.ts`
- `frontend/src/app/core/guard/admin-auth.guard.ts`
- `frontend/src/environments/environment.ts`
- `frontend/src/environments/environment.prod.ts`
- `frontend/README.md`
