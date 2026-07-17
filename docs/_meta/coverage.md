# Copertura delle fonti

La documentazione bootstrap copre i macro-area confermati dalla struttura della repository:

| Area | Fonti principali | Documenti |
|---|---|---|
| Backend | `backend/pom.xml`, `backend/src/main`, `backend/Dockerfile` | `docs/backend/overview.md`, `docs/security/runtime-security.md` |
| Frontend | `frontend/package.json`, `frontend/src`, `frontend/README.md` | `docs/frontend/overview.md`, `frontend/README.md` |
| Database | `backend/src/main/resources/db/migration`, properties | `docs/database/schema-and-migrations.md` |
| Architettura | backend, frontend, Docker, sandbox | `docs/architecture/overview.md` |
| Operations | README, Dockerfile, properties, package scripts | `docs/operations/local-and-production.md` |

Restano da verificare con un ambiente runtime disponibile: connessione MySQL locale, coerenza del default `spring.flyway.enabled=false` nel profilo locale e comportamento live dell'URL backend production.
