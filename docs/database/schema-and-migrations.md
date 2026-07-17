# Database e migration

## Scopo

MySQL conserva il dominio cinema e i dati di autenticazione e acquisto. Hibernate valida la compatibilità tra entity e schema; Flyway applica gli script SQL versionati quando è abilitato.

## Modello

Lo schema comprende `utente`, `admin`, `cliente`, `film`, `lingua`, `genere`, `sala`, `caratteristica_sala`, `fila`, `posto`, `programmazione`, `ordine` e `biglietto`, oltre alle tabelle di relazione film/genere e sala/caratteristica.

`programmazione` collega film e sala e conserva orario e prezzi pre/post 18:00. Un ordine collega l'utente, la programmazione e i biglietti; ogni biglietto identifica i posti selezionati.

## Sequenza Flyway

| Migration | Responsabilità |
|---|---|
| `V1` | crea lo schema relazionale |
| `V2` | seed dell'utente admin |
| `V3` | seed film, lingue e generi |
| `V4` | seed sale, caratteristiche, file e posti |
| `V5`–`V6` | seed programmazione giornaliera e settimanale |
| `V7` | aggiunge cliente e collega gli ordini all'utente |
| `V8` | aggiorna l'hash della password admin |

Le migration già applicate non devono essere modificate: ogni cambiamento successivo richiede un nuovo file `Vn__descrizione.sql`.

## Profili

Il profilo `prod` abilita Flyway e imposta `spring.jpa.hibernate.ddl-auto=validate`; il backend controlla lo schema senza modificarlo. Il profilo locale predefinito mantiene Flyway disabilitato e usa comunque `validate`, quindi la modalità locale deve essere scelta consapevolmente.

## Fonti

- `backend/src/main/resources/db/migration/V1__create_schema.sql`
- `backend/src/main/resources/db/migration/V7__add_cliente_and_order_user_fk.sql`
- `backend/src/main/resources/db/migration/V8__update_admin_password_hash.sql`
- `backend/src/main/resources/application.properties`
- `backend/src/main/resources/application-prod.properties`
