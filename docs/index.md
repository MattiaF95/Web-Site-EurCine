# Documentazione EurCine

Questa è la navigazione canonica della documentazione tecnica del progetto. Il root `README.md` descrive il prodotto e resta invariato; `frontend/README.md` contiene le istruzioni specifiche del client Angular.

## Aree

- [Architettura](architecture/overview.md) — confini tra frontend, backend, database e modalità di esecuzione.
- [Backend e API](backend/overview.md) — stack Spring Boot, endpoint, flussi applicativi e configurazione.
- [Frontend](frontend/overview.md) — Angular SSR, routing, guard, servizi e sandbox di sessione.
- [Database e migration](database/schema-and-migrations.md) — modello relazionale e Flyway V1–V8.
- [Sicurezza](security/runtime-security.md) — autenticazione, autorizzazione, CORS e read-only production.
- [Operations](operations/local-and-production.md) — avvio locale, Docker e deploy Render/Aiven.

## Metadati e copertura

- [Mappa documentale](_meta/documentation-map.json)
- [Stato del bootstrap](_meta/state.json)
- [Copertura delle fonti](_meta/coverage.md)

Le directory `docs/_archive/` e i file della skill sotto `.codex/` non fanno parte della documentazione attiva del progetto.
