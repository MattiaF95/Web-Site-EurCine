# Sviluppo e test

## Cosa copre questo documento

Raccoglie i comandi di sviluppo e le verifiche disponibili per backend, frontend e automazione documentale.

## Comandi principali

```bash
cd backend && ./mvnw test
cd frontend && npm test
cd frontend && npm run build
```

Il frontend espone anche `npm run generate:api` per generare client Angular da OpenAPI locale; la generazione deve essere usata solo quando il contratto API è disponibile su `http://localhost:8080/v3/api-docs`.

## Test presenti

Il backend include il test di `ReadOnlyModeFilter`. Il frontend include test per `SessionSandboxService` e `apiCacheInterceptor`, inclusi isolamento per token e reset del workspace al nuovo login.

## Regole di modifica

Per il backend mantenere il flusso controller-service-repository-model/dto, non modificare migration storiche per far passare un test e verificare sempre sicurezza/configurazione quando cambiano JWT, CORS o schema. Per il frontend usare componenti standalone, route lazy-loaded e lo stato locale già adottato.

## Automazione documentale

Il setup installa `.githooks/`, `tools/codebase-analysis-ai/`, `AGENTS.md` e `.github/workflows/codebase-analysis-ai.yml`. Il controllo deterministico è eseguibile con:

```bash
python tools/codebase-analysis-ai/check.py check --mode working-tree
```

## Relazioni

- [Configurazione e deploy](configuration-deployment.md)
- [Copertura documentale](../_meta/coverage.md)

## Fonti

- `backend/AGENTS.md`
- `frontend/AGENTS.md`
- `backend/src/test/java/eurcine/backend/security/ReadOnlyModeFilterTest.java`
- `frontend/src/app/core/service/session-sandbox.service.spec.ts`
- `frontend/src/app/core/interceptor/api-cache.interceptor.spec.ts`
- `frontend/package.json`

