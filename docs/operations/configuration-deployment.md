# Configurazione e deploy

## Cosa copre questo documento

Descrive l’avvio locale, la configurazione per ambiente e il deploy Docker/Render del backend e del frontend.

## Avvio locale

Prerequisiti: Java 21, MySQL sulla porta `3306`, Node.js e npm. Il backend usa `backend/src/main/resources/application-example.properties` come base; i valori sensibili devono essere forniti localmente o tramite variabili d’ambiente.

```bash
cd backend
./mvnw spring-boot:run

cd frontend
npm install
npm start
```

Il backend ascolta su `http://localhost:8080`, il frontend su `http://localhost:4200`.

## Profili e variabili

Le chiavi principali sono `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`, `CORS_ALLOWED_ORIGINS`, `JWT_SECRET`, `JWT_EXPIRATION_DAYS` e `PORT`. Non inserire valori reali nella documentazione o nel controllo versione.

Il profilo frontend locale usa `http://localhost:8080`; il profilo production usa l’endpoint Render configurato nel file environment del frontend.

## Docker e produzione

`backend/Dockerfile` compila il progetto con Maven su JDK 21, copia il JAR in un runtime JRE 21, espone la porta `8080` e imposta `SPRING_PROFILES_ACTIVE=prod`. Render deve ricevere le variabili d’ambiente necessarie; il database cloud indicato nel README è Aiven MySQL.

## Comportamento operativo

In produzione le letture usano il backend e le mutazioni vengono bloccate dal filtro server-side e simulate nel browser. La chiusura della tab elimina lo stato mock di sessione.

## Relazioni

- [Sicurezza](../security.md)
- [Database](../database.md)
- [Frontend](../frontend/overview.md)

## Fonti

- `README.md`
- `backend/Dockerfile`
- `backend/src/main/resources/application-example.properties`
- `backend/src/main/resources/application-prod.properties`
- `frontend/src/environments/environment.ts`
- `frontend/src/environments/environment.prod.ts`

