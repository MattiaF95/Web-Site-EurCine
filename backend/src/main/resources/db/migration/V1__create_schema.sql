CREATE TABLE IF NOT EXISTS lingua (
  id BIGINT NOT NULL AUTO_INCREMENT,
  nome VARCHAR(64) NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS genere (
  id BIGINT NOT NULL AUTO_INCREMENT,
  nome VARCHAR(64) NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS sala (
  id BIGINT NOT NULL AUTO_INCREMENT,
  nome VARCHAR(64) NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS utente (
  id BIGINT NOT NULL AUTO_INCREMENT,
  nome VARCHAR(64) NOT NULL,
  cognome VARCHAR(64) NOT NULL,
  email VARCHAR(160) NOT NULL,
  password_hash VARCHAR(255) NOT NULL,
  created_at DATETIME(6) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY uk_utente_email (email)
);

CREATE TABLE IF NOT EXISTS admin (
  id BIGINT NOT NULL,
  ruolo VARCHAR(64) NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT fk_admin_utente FOREIGN KEY (id) REFERENCES utente (id)
);

CREATE TABLE IF NOT EXISTS film (
  id BIGINT NOT NULL AUTO_INCREMENT,
  titolo VARCHAR(255) NOT NULL,
  durata_min INT NOT NULL,
  lingua_id BIGINT NOT NULL,
  PRIMARY KEY (id),
  KEY idx_film_lingua_id (lingua_id),
  CONSTRAINT fk_film_lingua FOREIGN KEY (lingua_id) REFERENCES lingua (id)
);

CREATE TABLE IF NOT EXISTS film_genere (
  film_id BIGINT NOT NULL,
  genere_id BIGINT NOT NULL,
  PRIMARY KEY (film_id, genere_id),
  KEY idx_film_genere_genere_id (genere_id),
  CONSTRAINT fk_film_genere_film FOREIGN KEY (film_id) REFERENCES film (id),
  CONSTRAINT fk_film_genere_genere FOREIGN KEY (genere_id) REFERENCES genere (id)
);

CREATE TABLE IF NOT EXISTS fila (
  id BIGINT NOT NULL AUTO_INCREMENT,
  sala_id BIGINT NOT NULL,
  lettera VARCHAR(4) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY uk_fila_sala_lettera (sala_id, lettera),
  KEY idx_fila_sala_id (sala_id),
  CONSTRAINT fk_fila_sala FOREIGN KEY (sala_id) REFERENCES sala (id)
);

CREATE TABLE IF NOT EXISTS posto (
  id BIGINT NOT NULL AUTO_INCREMENT,
  fila_id BIGINT NOT NULL,
  numero INT NOT NULL,
  attivo BIT(1) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY uk_posto_fila_numero (fila_id, numero),
  KEY idx_posto_fila_id (fila_id),
  CONSTRAINT fk_posto_fila FOREIGN KEY (fila_id) REFERENCES fila (id)
);

CREATE TABLE IF NOT EXISTS programmazione (
  id BIGINT NOT NULL AUTO_INCREMENT,
  film_id BIGINT NOT NULL,
  sala_id BIGINT NOT NULL,
  start_at DATETIME(6) NOT NULL,
  prezzo_base_pre18 DECIMAL(10,2) NOT NULL,
  prezzo_base_post18 DECIMAL(10,2) NOT NULL,
  PRIMARY KEY (id),
  KEY idx_programmazione_film_id (film_id),
  KEY idx_programmazione_sala_id (sala_id),
  KEY idx_programmazione_sala_start_at (sala_id, start_at),
  KEY idx_programmazione_film_start_at (film_id, start_at),
  CONSTRAINT fk_programmazione_film FOREIGN KEY (film_id) REFERENCES film (id),
  CONSTRAINT fk_programmazione_sala FOREIGN KEY (sala_id) REFERENCES sala (id)
);

CREATE TABLE IF NOT EXISTS ordine (
  id BIGINT NOT NULL AUTO_INCREMENT,
  numero_ordine VARCHAR(64) NOT NULL,
  nome_cliente VARCHAR(128) NOT NULL,
  totale DECIMAL(10,2) NOT NULL,
  created_at DATETIME(6) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY uk_ordine_numero_ordine (numero_ordine)
);

CREATE TABLE IF NOT EXISTS biglietto (
  id BIGINT NOT NULL AUTO_INCREMENT,
  ordine_id BIGINT NOT NULL,
  programmazione_id BIGINT NOT NULL,
  posto_id BIGINT NOT NULL,
  prezzo DECIMAL(10,2) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY uk_biglietto_programmazione_posto (programmazione_id, posto_id),
  KEY idx_biglietto_ordine_id (ordine_id),
  KEY idx_biglietto_programmazione_id (programmazione_id),
  KEY idx_biglietto_posto_id (posto_id),
  KEY idx_biglietto_programmazione_ordine (programmazione_id, ordine_id),
  CONSTRAINT fk_biglietto_ordine FOREIGN KEY (ordine_id) REFERENCES ordine (id),
  CONSTRAINT fk_biglietto_programmazione FOREIGN KEY (programmazione_id) REFERENCES programmazione (id),
  CONSTRAINT fk_biglietto_posto FOREIGN KEY (posto_id) REFERENCES posto (id)
);

CREATE TABLE IF NOT EXISTS admin_session (
  id BIGINT NOT NULL AUTO_INCREMENT,
  token VARCHAR(255) NOT NULL,
  admin_id BIGINT NOT NULL,
  created_at DATETIME(6) NOT NULL,
  expires_at DATETIME(6) NOT NULL,
  revoked BIT(1) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY uk_admin_session_token (token),
  KEY idx_admin_session_admin (admin_id),
  CONSTRAINT fk_admin_session_admin FOREIGN KEY (admin_id) REFERENCES admin (id)
);
