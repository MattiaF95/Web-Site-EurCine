# Flussi applicativi

## Cosa copre questo documento

Raccoglie i flussi che attraversano più moduli: autenticazione, consultazione della programmazione, acquisto, amministrazione e comportamento read-only in produzione.

## Autenticazione

In locale `AuthService` del frontend usa `/api/auth/login`, `/api/auth/register`, `/api/auth/me` e `/api/auth/logout`. Il backend resta stateless e usa JWT tramite `JwtCookieAuthenticationFilter`; il client conserva il token nel proprio storage e lo aggiunge alle richieste tramite interceptor.

In produzione, quando la sandbox è attiva, `SessionSandboxService` crea una sessione mock isolata per token. La directory utenti e ogni workspace sono salvati in `sessionStorage`, quindi non sono persistenti oltre la sessione del browser.

## Acquisto biglietti

1. L’utente apre una programmazione e la mappa posti tramite `GET /api/programmazione/{programmazioneId}/seat-map`.
2. `OrdineService` calcola il prezzo in base all’orario pre/post 18:00.
3. In locale invia `POST /api/ordini` al backend.
4. In produzione crea ordine e biglietti nel workspace della sandbox, aggiorna la cache della mappa posti e mostra il codice ordine.

## Gestione amministrativa

Le route frontend sotto `/admin` richiedono `adminAuthGuard`. Il backend applica `hasAnyRole("ADMIN", "SUPER_ADMIN")` a `/api/admin/**`. Le operazioni di film e programmazione usano servizi distinti lato client e lato server; in produzione le mutazioni restano locali alla sandbox.

## Read-only di produzione

Con `app.read-only-mode=true`, `ReadOnlyModeFilter` lascia passare `GET`, `HEAD` e `OPTIONS`, ma risponde `405 Method Not Allowed` alle altre richieste. Il frontend intercetta il caso di produzione e simula le mutazioni in `sessionStorage`.

## Cache e isolamento

`apiCacheInterceptor` applica TTL diversi a film, sale, programmazione, home e metadati admin. Le chiavi includono il token di sessione; le mutazioni invalida-no le cache coinvolte. I test frontend verificano l’isolamento tra token e il reset al nuovo login.

## Relazioni

- [Sicurezza](../security.md)
- [API](../api.md)
- [Frontend](../frontend/overview.md)

## Fonti

- `backend/src/main/java/eurcine/backend/controller/AuthController.java`
- `backend/src/main/java/eurcine/backend/security/ReadOnlyModeFilter.java`
- `frontend/src/app/core/service/session-sandbox.service.ts`
- `frontend/src/app/core/service/ordine.service.ts`
- `frontend/src/app/core/interceptor/api-cache.interceptor.ts`

