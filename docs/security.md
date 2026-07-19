# Sicurezza

## Cosa copre questo documento

Descrive autenticazione, autorizzazione, CORS, modalità read-only e principali confini di sicurezza verificati nel codice.

## Contesto

Il backend è stateless: Spring Security non usa sessioni server-side, HTTP Basic e form login sono disabilitati. Il frontend mantiene una sandbox separata per la produzione read-only.

## Come funziona

`SecurityConfig` applica `SecurityFilterChain`, `JwtCookieAuthenticationFilter`, `RestAuthenticationEntryPoint` e `RestAccessDeniedHandler`. CORS usa `app.cors.allowed-origins` e ricade su localhost in locale o sul dominio Render nel profilo `prod`.

## Controlli attivi

- `/api/ordini/**` richiede autenticazione.
- `/api/admin/**` richiede `ADMIN` o `SUPER_ADMIN`.
- JWT identifica l’utente e il ruolo.
- `ReadOnlyModeFilter` blocca in produzione i metodi diversi da `GET`, `HEAD` e `OPTIONS` con `405`.
- Validazione server-side e repository JPA parametrizzati riducono input non validi e SQL injection.
- Swagger/OpenAPI è disabilitato nel profilo `prod`.

## Limiti e attenzione operativa

La sandbox è una simulazione client-side: i dati mock sono visibili al browser e vengono persi con la sessione. Non deve essere considerata una persistenza o un controllo di sicurezza server-side. Le credenziali e i segreti non devono essere copiati nella documentazione; usare le variabili d’ambiente previste.

## Relazioni

- [Flussi applicativi](architecture/flows.md)
- [Configurazione e deploy](operations/configuration-deployment.md)
- [API](api.md)

## Fonti

- `backend/src/main/java/eurcine/backend/config/SecurityConfig.java`
- `backend/src/main/java/eurcine/backend/security/JwtCookieAuthenticationFilter.java`
- `backend/src/main/java/eurcine/backend/security/ReadOnlyModeFilter.java`
- `frontend/src/app/core/service/session-sandbox.service.ts`
- `frontend/src/app/core/guard/admin-auth.guard.ts`
- `frontend/src/app/core/guard/user-auth.guard.ts`

