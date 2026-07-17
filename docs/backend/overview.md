# Backend e API

## Scopo

Il backend è un'applicazione Spring Boot 4.0.6 su Java 21. Espone API REST per autenticazione, catalogo film, sale, programmazione, mappa posti, ordini e gestione amministrativa.

## Stack e struttura

Spring Web MVC gestisce HTTP, Spring Data JPA/Hibernate la persistenza, Spring Security l'accesso, JJWT i token, Flyway le migration e SpringDoc la documentazione OpenAPI locale. Il flusso applicativo segue `controller → service → repository → model/DTO`.

## Endpoint principali

| Area | Base path | Accesso |
|---|---|---|
| Autenticazione | `/api/auth` | login pubblico; `me` e logout autenticati |
| Film | `/api/film` | pubblico |
| Sale | `/api/sale` | pubblico |
| Programmazione | `/api/programmazione` | pubblico |
| Ordini | `/api/ordini` | autenticato |
| Amministrazione | `/api/admin` | `ADMIN` o `SUPER_ADMIN` |
| Home aggregata | `/api/home` | pubblico |

L'endpoint `/api/programmazione/{id}/seat-map` espone la mappa dei posti; l'ordine viene creato da `OrdineService`, che valida posti duplicati, appartenenza alla sala, occupazione e prezzo in base all'orario.

## Autenticazione

`AuthService` gestisce login e registrazione. `JwtCookieAuthenticationFilter` legge un bearer token o il cookie `eurcine_session`; `JwtService` verifica il token e ricostruisce l'identità e il ruolo. La sessione Spring resta stateless.

## Configurazione

- `backend/src/main/resources/application.properties` è il profilo locale predefinito.
- `backend/src/main/resources/application-prod.properties` abilita read-only, Flyway e disabilita Swagger/OpenAPI.
- Le variabili sensibili sono `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`, `JWT_SECRET`, `JWT_EXPIRATION_DAYS` e `CORS_ALLOWED_ORIGINS`.

## Fonti

- `backend/src/main/java/eurcine/backend/config/SecurityConfig.java`
- `backend/src/main/java/eurcine/backend/controller/AuthController.java`
- `backend/src/main/java/eurcine/backend/controller/AdminManagementController.java`
- `backend/src/main/java/eurcine/backend/service/OrdineService.java`
- `backend/src/main/resources/application.properties`
- `backend/src/main/resources/application-prod.properties`
