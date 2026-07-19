# API REST

## Cosa copre questo documento

Elenca gli endpoint REST implementati dai controller Spring Boot e il loro requisito di autenticazione.

## Endpoint pubblici

| Area | Endpoint | Operazione |
|---|---|---|
| Home | `/api/home` | programmazione giornaliera |
| Film | `/api/film`, `/api/film/{titolo}` | catalogo e dettaglio |
| Sale | `/api/sale`, `/api/sale/{nome}` | sale e dettaglio |
| Programmazione | `/api/programmazione`, `/api/programmazione/date-disponibili` | elenco e date |
| Posti | `/api/programmazione/{programmazioneId}/seat-map` | mappa sala |
| Auth | `/api/auth/login`, `/api/auth/register` | accesso e registrazione |

## Endpoint autenticati

| Area | Endpoint | Requisito |
|---|---|---|
| Auth | `/api/auth/me`, `/api/auth/logout` | utente autenticato |
| Ordini | `/api/ordini`, `/api/ordini/codice/{numeroOrdine}`, `/api/ordini/codice/{numeroOrdine}/biglietti` | utente autenticato |
| Ordini | `POST /api/ordini` | utente autenticato; disabilitato in prod read-only |

## Endpoint admin

`/api/admin/**` richiede ruolo `ADMIN` o `SUPER_ADMIN`. Comprende catalogo/metadati film, CRUD film e creazione/cancellazione della programmazione.

## Errori e documentazione interattiva

Gli errori applicativi passano da `ApiExceptionHandler`. OpenAPI/Swagger è abilitato nella configurazione di esempio e disabilitato nel profilo `prod`; in locale l’interfaccia è disponibile su `/swagger-ui.html`.

## Relazioni

- [Backend](backend/overview.md)
- [Sicurezza](security.md)
- [Flussi applicativi](architecture/flows.md)

## Fonti

- `backend/src/main/java/eurcine/backend/controller/`
- `backend/src/main/java/eurcine/backend/config/SecurityConfig.java`
- `backend/src/main/java/eurcine/backend/controller/ApiExceptionHandler.java`
- `backend/src/main/resources/application-example.properties`
- `backend/src/main/resources/application-prod.properties`

