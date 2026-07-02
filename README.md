# 🎬 EurCine — Sito Cinema Didattico

Progetto creato **per divertimento** e per testare concretamente l'uso di agenti AI come **CODEX** nella scrittura di codice a partire da specifiche tecniche dettagliate. L'idea, l'architettura, il design del dominio e tutte le decisioni tecniche sono state fatte da me — CODEX si è occupato della scrittura del codice e dei test.

Il risultato? Il sito completo è stato realizzato in **2/3 giorni**, poi rifinito e stabilizzato dopo il primo deploy. Un esperimento riuscito, e pure divertente.

🔗 **Live:** [https://web-site-eurcine-1.onrender.com/home](https://web-site-eurcine-1.onrender.com/home)

⏱️ **Nota:** il sito gira su Render con piano gratuito e sia il backend che il DB vanno in **cold start**. Se è la prima visita dopo un periodo di inattività, backend e database devono avviarsi.  
Attendere **4/5 minuti** prima che tutto sia operativo. 

---

## 🧱 Stack Tecnologico

### Backend
| Tecnologia | Versione |
|---|---|
| ☕ Java | 21 |
| 🌱 Spring Boot | 4.0.6 |
| 🗄️ Spring Data JPA + Hibernate | — |
| 🔐 Spring Security + JWT (jjwt) | 0.12.6 |
| 🐬 MySQL | — |
| 🦋 Flyway | migrations versionate |
| 📖 SpringDoc OpenAPI (Swagger) | 2.8.9 |
| 🏗️ Lombok | — |
| 🐳 Docker | multi-stage build |

### Frontend
| Tecnologia | Versione |
|---|---|
| 🅰️ Angular | 21.2 |
| 🖥️ Angular SSR | 21.2 (Express 5) |
| 🎨 SCSS | — |
| 🧪 Vitest | 4.x |
| 💅 Prettier | 3.x |

### Infrastruttura (produzione)
| Servizio | Utilizzo |
|---|---|
| ☁️ Render | Hosting backend (Docker) + frontend |
| 🐬 Aiven.io | MySQL cloud gratuito |

---

## 🎭 Cosa puoi fare sul sito

Il sito simula un cinema reale con le funzionalità principali:

- 🎥 **Sfogliare i film** in programmazione e leggere le trame
- 📅 **Vedere la programmazione settimanale** con orari e sale
- 🪑 **Selezionare i posti** direttamente sulla mappa interattiva della sala
- 🎟️ **Acquistare biglietti** (prezzi differenziati pre/post 18:00)
- 👤 **Registrarsi** e creare il proprio account cliente
- 📦 **Consultare i propri ordini** nell'area personale
- 🏛️ **Esplorare le sale** con caratteristiche tecniche (Dolby Atmos, 4DX, IMAX, ecc.)

### Area Admin 🔑

Esiste un'area amministrativa protetta per:
- Aggiungere, modificare ed eliminare film
- Gestire la programmazione settimanale
- Visionare gli ordini di ogni utente registrato

**Credenziali di accesso admin:**
```text
Email:    admin@eurcine.it
Password: admin123
```

> ⚠️ **Vi chiedo di non modificare in maniera brutale il database.** Usatelo per giocare, testare e verificare — è un progetto didattico e voglio che resti funzionante per chiunque voglia esplorarlo. Grazie! 🙏

---

## 🔒 Security

Il backend implementa un layer di sicurezza su più livelli.

### Autenticazione — JWT via Cookie
Il token JWT viene trasportato in un cookie `HttpOnly` (`eurcine_session`) oppure come `Bearer` header. Il filtro `JwtCookieAuthenticationFilter` intercetta ogni richiesta, verifica la firma del token e popola il `SecurityContext` con identità e ruolo dell'utente. La sessione è completamente **stateless**: nessuna sessione server-side, nessun `HttpSession`.

### Autorizzazione per ruolo
Le route sono protette a livello di `SecurityFilterChain`:
- `/api/ordini/**` → richiede autenticazione
- `/api/admin/**` → richiede ruolo `ADMIN` o `SUPER_ADMIN`
- Il resto delle API pubbliche (film, sale, programmazione) è accessibile senza autenticazione

HTTP Basic e form login sono **esplicitamente disabilitati**.

### SQL Injection
Tutta l'interazione col database passa attraverso **Spring Data JPA** con query parametrizzate (JPQL e query derivate). Non esistono query SQL costruite per concatenazione di stringhe — Hibernate usa prepared statements per default, rendendo di fatto inutile qualsiasi tentativo di SQL injection classico.

### CORS
Le origin autorizzate sono configurate tramite variabile d'ambiente (`CORS_ALLOWED_ORIGINS`). In produzione punta esclusivamente al dominio del frontend su Render — richieste da altri domini vengono bloccate a livello di preflight.

### Swagger UI — solo locale
La Swagger UI (`/swagger-ui.html`) è attiva **solo in locale**, dove potete usarla per testare gli endpoint, fare verifiche manuali e magari anche penetration test sull'API. In produzione (profilo `prod`) è disabilitata (ma potete attivarla sull'eventuale vostro progetto deployato, facendo dei penetration test più realistici):

```properties
# application-prod.properties
springdoc.api-docs.enabled=false
springdoc.swagger-ui.enabled=false
```

In locale è raggiungibile su `http://localhost:8080/swagger-ui.html`.

---

## 🗃️ Database — Hibernate e Flyway

Il progetto usa **due meccanismi distinti** per la gestione dello schema, con comportamenti diversi a seconda dell'ambiente.

### Come funzionano insieme

**Hibernate** (`spring.jpa.hibernate.ddl-auto`) controlla se Hibernate può modificare lo schema del DB:
- `update` — Hibernate crea/aggiorna le tabelle confrontando le entity JPA con lo schema attuale. Utile in locale per sviluppo rapido, non adatto per produzione.
- `validate` — Hibernate non tocca nulla: controlla solo che lo schema esistente sia coerente con le entity. Se c'è una discrepanza, l'avvio fallisce. Usato in produzione.
- `none` — Hibernate non fa niente sullo schema.

**Flyway** (`spring.flyway.enabled`) esegue le migration SQL versionate in `db/migration/` all'avvio dell'applicazione:
- Ogni script `V{n}__descrizione.sql` viene eseguito una sola volta, in ordine, e tracciato nella tabella `flyway_schema_history`.
- Se uno script è già stato applicato, Flyway lo salta.
- Gestisce sia la creazione dello schema che il seed dei dati (film, sale, programmazione, admin).

### Regola da seguire: non usarli contemporaneamente

Se Flyway è abilitato (`flyway.enabled=true`), imposta **sempre** `ddl-auto=validate` o `ddl-auto=none`. Se li usi entrambi attivi con `ddl-auto=update`, Hibernate e Flyway possono entrare in conflitto modificando lo schema in modo incoerente.

### Configurazione per ambiente

| | Locale (default) | Locale con seed | Produzione |
|---|---|---|---|
| `ddl-auto` | `update` | `none` | `validate` |
| `flyway.enabled` | `false` | `true` | `true` |
| Risultato | Hibernate crea le tabelle, nessun dato di seed | Flyway esegue V1–V7, db completo con dati | Flyway gestisce tutto, Hibernate solo controlla |

### Script di migration

| Script | Contenuto |
|---|---|
| `V1__create_schema.sql` | Struttura completa di tutte le tabelle |
| `V2__seed_admin.sql` | Inserimento admin di default |
| `V3__seed_film.sql` | Catalogo film con generi e lingue |
| `V4__seed_sale.sql` | Sale cinema con file, posti e caratteristiche tecniche |
| `V5__seed_programmazione_daily.sql` | Programmazione giornaliera di esempio |
| `V6__seed_programmazione_weekly.sql` | Programmazione settimanale estesa |
| `V7__add_cliente_and_order_user_fk.sql` | Tabella cliente e FK ordini |

---

## 🏠 Run in Locale

### Prerequisiti
- **Java 21**
- **Maven** (o usa `./mvnw` incluso nel progetto)
- **MySQL** installato e in esecuzione su porta `3306`
- **Node.js** + **npm 11+** per il frontend

### 1. Configurare il Backend

Copia il file di esempio e rinominalo:

```bash
cd backend/src/main/resources
cp application-example.properties application.properties
```

Il file usa già valori di default per locale:

```properties
spring.datasource.url=jdbc:mysql://127.0.0.1:3306/eurcine?createDatabaseIfNotExist=true&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=root
app.cors.allowed-origins=http://localhost:4200
app.jwt.secret=change-this-example-secret-at-least-32-characters
app.jwt.expiration-days=7

# Locale: Hibernate gestisce lo schema, nessun dato di seed
spring.jpa.hibernate.ddl-auto=update
spring.flyway.enabled=false
```

> Se il tuo MySQL ha credenziali diverse, modifica `spring.datasource.username` e `spring.datasource.password`.

**Vuoi avere anche i dati di seed in locale** (film, sale, programmazione)? Modifica così:

```properties
spring.jpa.hibernate.ddl-auto=none
spring.flyway.enabled=true
```

Flyway eseguirà tutti gli script V1–V7 al primo avvio e il DB sarà completo con tutti i dati.

### 2. Avviare il Backend

```bash
cd backend
./mvnw spring-boot:run
```

Backend su `http://localhost:8080`.  
Swagger UI su `http://localhost:8080/swagger-ui.html`.

### 3. Avviare il Frontend

```bash
cd frontend
npm install
npm start
```

Frontend su `http://localhost:4200`.

---

## ☁️ Deploy in Produzione (Render + Aiven)

In produzione il backend usa il profilo `prod` (`application-prod.properties`), attivato automaticamente dal Dockerfile:

```dockerfile
ENV SPRING_PROFILES_ACTIVE=prod
```

Tutte le configurazioni sensibili vengono iniettate come **variabili d'ambiente** nel servizio Render. Non modificare `application-prod.properties` — legge tutto da env vars.

### Variabili d'ambiente da configurare su Render

| Variabile | Descrizione | Esempio |
|---|---|---|
| `DB_URL` | JDBC URL del database Aiven | `jdbc:mysql://host:port/eurcine?ssl-mode=REQUIRED&serverTimezone=UTC` |
| `DB_USERNAME` | Username MySQL Aiven | `avnadmin` |
| `DB_PASSWORD` | Password MySQL Aiven | `...` |
| `JWT_SECRET` | Segreto JWT (min 32 char) | `una-stringa-lunga-e-casuale` |
| `JWT_EXPIRATION_DAYS` | Durata token in giorni | `7` |
| `CORS_ALLOWED_ORIGINS` | Origin del frontend Angular | `https://tuo-sito.onrender.com` |
| `PORT` | Porta app (Render la imposta automaticamente) | `8080` |

### Flyway in produzione

Flyway è **abilitato** e gestisce tutto all'avvio:
- Crea le tabelle se non esistono
- Applica le migration in sequenza versionate
- `ddl-auto=validate` — Hibernate verifica solo la coerenza tra entity e schema, non modifica nulla

### Dockerfile (Backend)

Il Dockerfile è stato creato per il deploy su Render perché quest'ultimo non supporta Java/Spring Boot nativo.

Multi-stage build: Maven compila il JAR nel primo stage, il secondo stage usa solo la JRE minimale per eseguirlo.

```bash
# Build manuale (Render lo fa in automatico dal repo)
cd backend
docker build -t eurcine-backend .
docker run -e DB_URL=... -e DB_USERNAME=... -e DB_PASSWORD=... -p 8080:8080 eurcine-backend
```

---

## 🤖 Sviluppato con CODEX

Questo progetto è stato **ideato, progettato e architettato da me** — struttura del dominio, scelte tecnologiche, decisioni di design, flusso applicativo. Tutto definito prima di iniziare a scrivere codice. Durante la creazione sono state apportate modifiche ed implementazioni varie, ampliando il progetto di base (ad esempio in ambito security o la gestione dei film e della programmazione con un account Admin).

**La scrittura del codice e dei test è stata delegata a CODEX** (OpenAI Codex), usato come agente di sviluppo. L'obiettivo era verificare quanto velocemente un agente AI potesse tradurre una specifica tecnica dettagliata in codice funzionante e deployabile. Il sito completo è stato realizzato in **2/3 giorni**, poi rifinito e stabilizzato dopo il primo deploy in produzione.

Un esperimento che consiglio a chiunque voglia capire come lavorare efficacemente con gli agenti AI: la qualità del risultato dipende quasi interamente dalla qualità delle specifiche che dai in input.

Il file `backend/dev.md` contiene tutte le note di sviluppo: entity design, schema DB, indici, decisioni architetturali e dati di esempio usati per il seed.

---

## 📁 Struttura Repository

```text
Web-Site-EurCine/
├── backend/                  # Spring Boot API
│   ├── src/main/java/eurcine/backend/
│   │   ├── config/           # SecurityConfig
│   │   ├── controller/       # REST controllers
│   │   ├── dto/              # Request/Response DTOs
│   │   ├── model/            # JPA entities
│   │   ├── repository/       # Spring Data repositories
│   │   ├── security/         # JWT filter, CSRF, handlers
│   │   └── service/          # Business logic
│   ├── src/main/resources/
│   │   ├── db/migration/     # Script Flyway V1–V7
│   │   ├── application-example.properties
│   │   └── application-prod.properties
│   └── Dockerfile
└── frontend/                 # Angular 21 + SSR
    └── src/app/
        ├── core/             # guard, interceptor, model, service
        ├── components/       # componenti riutilizzabili
        └── pages/            # pagine (home, film, sale, admin, ...)
```

## Screenshot

<table>
  <tr>
    <td align="center">
      <a href="frontend/public/assets/imgReadme/HOME.webp">
        <img src="frontend/public/assets/imgReadme/HOME.webp" alt="Home" width="300" />
      </a>
    </td>
    <td align="center">
      <a href="frontend/public/assets/imgReadme/Programmazione-Film.webp">
        <img src="frontend/public/assets/imgReadme/Programmazione-Film.webp" alt="Programmazione" width="300" />
      </a>
    </td>
    <td align="center">
      <a href="frontend/public/assets/imgReadme/Prenotazione-Posti-Film-Sala.webp">
        <img src="frontend/public/assets/imgReadme/Prenotazione-Posti-Film-Sala.webp" alt="Scelta posti" width="300" />
      </a>
    </td>
  </tr>
  <tr>
    <td align="center">
      <a href="frontend/public/assets/imgReadme/Lista-Film.webp">
        <img src="frontend/public/assets/imgReadme/Lista-Film.webp" alt="Lista film" width="300" />
      </a>
    </td>
    <td align="center">
      <a href="frontend/public/assets/imgReadme/Lista-Sale-Cinema.webp">
        <img src="frontend/public/assets/imgReadme/Lista-Sale-Cinema.webp" alt="Lista sale cinema" width="300" />
      </a>
    </td>
    <td align="center">
      <a href="frontend/public/assets/imgReadme/Dettaglio-Ordine.webp">
        <img src="frontend/public/assets/imgReadme/Dettaglio-Ordine.webp" alt="Dettaglio ordine" width="300" />
      </a>
    </td>
  </tr>
  <tr>
    <td align="center">
      <a href="frontend/public/assets/imgReadme/Ordini-Admin.webp">
        <img src="frontend/public/assets/imgReadme/Ordini-Admin.webp" alt="Ordini admin" width="300" />
      </a>
    </td>
    <td align="center">
      <a href="frontend/public/assets/imgReadme/Gestione-Film.webp">
        <img src="frontend/public/assets/imgReadme/Gestione-Film.webp" alt="Gestione film" width="300" />
      </a>
    </td>
    <td align="center">
      <a href="frontend/public/assets/imgReadme/Gestione-Programmazione.webp">
        <img src="frontend/public/assets/imgReadme/Gestione-Programmazione.webp" alt="Gestione programmazione" width="300" />
      </a>
    </td>
  </tr>
</table>

## Crediti

- Sviluppo, test, deploy: [@MattiaF95](https://github.com/MattiaF95)