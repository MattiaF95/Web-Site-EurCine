CREATE TABLE IF NOT EXISTS cliente (
  id BIGINT NOT NULL,
  ruolo VARCHAR(64) NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT fk_cliente_utente FOREIGN KEY (id) REFERENCES utente (id)
);

INSERT INTO utente (nome, cognome, email, password_hash, created_at)
SELECT 'Legacy', 'Orders', 'legacy.orders@eurcine.local', '$2a$10$VgP5bVY9aK2nG4nYd5mAA.UW5xI86pvf8Q0jBYiQ6IUl0fGf7q6Wm', NOW()
WHERE NOT EXISTS (
  SELECT 1 FROM utente u WHERE u.email = 'legacy.orders@eurcine.local'
);

INSERT INTO cliente (id, ruolo)
SELECT u.id, 'USER'
FROM utente u
WHERE u.email = 'legacy.orders@eurcine.local'
  AND NOT EXISTS (SELECT 1 FROM cliente c WHERE c.id = u.id);

ALTER TABLE ordine
  ADD COLUMN utente_id BIGINT NULL AFTER nome_cliente;

UPDATE ordine
SET utente_id = (SELECT id FROM utente WHERE email = 'legacy.orders@eurcine.local')
WHERE utente_id IS NULL;

ALTER TABLE ordine
  MODIFY utente_id BIGINT NOT NULL,
  ADD KEY idx_ordine_utente_id (utente_id),
  ADD CONSTRAINT fk_ordine_utente FOREIGN KEY (utente_id) REFERENCES utente (id);
