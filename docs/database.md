# Database e migration

## Cosa copre questo documento

Descrive il modello persistente MySQL e l’uso di Hibernate e Flyway nei profili locali e di produzione.

## Contesto

Le entity JPA in `backend/src/main/java/eurcine/backend/model/` rappresentano utenti, film, generi, lingue, sale, file, posti, programmazione, ordini e biglietti. Le migration SQL in `backend/src/main/resources/db/migration/` sono la fonte di verità per lo schema runtime quando Flyway è abilitato.

## Come funziona

Le migration `V{n}__descrizione.sql` vengono applicate una volta e registrate da Flyway. In locale la configurazione di esempio usa `ddl-auto=update` e Flyway abilitato; il README distingue inoltre il profilo locale con seed. In produzione `ddl-auto=validate`, Flyway è attivo e `app.read-only-mode=true`.

## Sequenza delle migration

1. `V1__create_schema.sql` crea lo schema.
2. `V2__seed_admin.sql` inserisce il seed amministrativo.
3. `V3__seed_film.sql` carica il catalogo.
4. `V4__seed_sale.sql` carica sale e posti.
5. `V5__seed_programmazione_daily.sql` carica programmazione giornaliera.
6. `V6__seed_programmazione_weekly.sql` carica programmazione settimanale.
7. `V7__add_cliente_and_order_user_fk.sql` aggiunge cliente e relazione ordini-utente.

## Regole di sviluppo

Quando cambia lo schema, aggiornare prima Flyway e poi allineare entity, repository, DTO e servizi. Non usare H2 per validare migration che dipendono da sintassi MySQL-specifica.

## Relazioni

- [Backend](backend/overview.md)
- [Configurazione e deploy](operations/configuration-deployment.md)
- [Ragionamento di sviluppo](development-reasoning.md)

## Fonti

- `backend/src/main/java/eurcine/backend/model/`
- `backend/src/main/resources/db/migration/`
- `backend/src/main/resources/application-example.properties`
- `backend/src/main/resources/application-prod.properties`
- `backend/AGENTS.md`

