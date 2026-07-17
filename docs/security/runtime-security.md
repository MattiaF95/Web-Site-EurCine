# Sicurezza e modalità runtime

## Autenticazione e autorizzazione

`SecurityConfig` usa una `SecurityFilterChain` stateless. Il login, Swagger locale e gli endpoint pubblici sono accessibili senza autenticazione; `/api/auth/me`, logout e `/api/ordini/**` richiedono un utente autenticato; `/api/admin/**` richiede `ADMIN` o `SUPER_ADMIN`. HTTP Basic e form login sono disabilitati.

I token JWT possono arrivare dall'header bearer o dal cookie `eurcine_session`. La scadenza e il segreto sono configurabili tramite environment; il segreto non deve essere committato.

## Read-only in produzione

`application-prod.properties` imposta `app.read-only-mode=true`. `ReadOnlyModeFilter` consente le richieste sicure di lettura e risponde `405` alle mutazioni prima dei service e dei repository. Flyway resta l'unico meccanismo previsto per modificare schema e seed.

Il frontend mantiene una sandbox `sessionStorage` per simulare login, registrazione, ordini e modifiche admin durante le sessioni di produzione. Questo stato è locale alla tab e non è una misura di persistenza o di autorizzazione backend.

## CORS e documentazione API

Le origin autorizzate derivano da `CORS_ALLOWED_ORIGINS`, con fallback distinti per locale e produzione. Swagger/OpenAPI è disabilitato nel profilo `prod` e destinato alle verifiche locali.

## Da verificare

- Il frontend contiene un `csrf.interceptor.ts`, ma la configurazione corrente non lo registra tra i provider di `appConfig`.
- Il backend configura `allowCredentials=false` mentre il filtro JWT supporta anche cookie; il contratto effettivo va verificato con un test runtime.

## Fonti

- `backend/src/main/java/eurcine/backend/config/SecurityConfig.java`
- `backend/src/main/java/eurcine/backend/security/JwtCookieAuthenticationFilter.java`
- `backend/src/main/java/eurcine/backend/security/ReadOnlyModeFilter.java`
- `backend/src/main/resources/application-prod.properties`
- `frontend/src/app/core/service/session-sandbox.service.ts`
