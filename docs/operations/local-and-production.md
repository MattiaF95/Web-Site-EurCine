# Operations: locale e produzione

## Avvio locale

Prerequisiti: Java 21, MySQL su porta 3306 e Node.js/npm. Il backend si avvia da `backend/` con `./mvnw spring-boot:run`; il frontend da `frontend/` con `npm install` e `npm start`.

Il backend ascolta sulla porta `8080` (o sulla variabile `PORT`). Swagger locale è disponibile su `/swagger-ui.html` quando il profilo lo abilita.

## Build e container

`backend/Dockerfile` esegue una build Maven multi-stage con Temurin 21 e produce un'immagine runtime JRE Temurin 21. Il container espone la porta 8080 e imposta `SPRING_PROFILES_ACTIVE=prod`.

## Deploy

Il README root documenta il deploy su Render con database MySQL Aiven. Le credenziali e gli URL devono essere configurati come variabili d'ambiente del servizio; non vanno inseriti nei file versionati.

## Verifiche

- Checker documentale: `python tools/codebase-analysis-ai/check.py check --mode working-tree`.
- Build frontend: `cd frontend && npm run build`.
- Test backend: usare il wrapper Maven in `backend/` secondo la configurazione MySQL disponibile.

La verifica runtime con un database reale non è stata eseguita durante questo bootstrap; i dettagli operativi derivano dai file di configurazione e dal README canonico.

## Fonti

- `README.md`
- `backend/Dockerfile`
- `backend/src/main/resources/application-prod.properties`
- `frontend/package.json`
- `tools/codebase-analysis-ai/check.py`
