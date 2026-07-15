-- Seed admin utente (idempotente)

INSERT INTO utente (nome, cognome, email, password_hash, created_at)
SELECT 'Admin', 'Eurcine', 'admin@eurcine.it', 'admin123', NOW()
WHERE NOT EXISTS (
  SELECT 1 FROM utente u WHERE u.email = 'admin@eurcine.it'
);

INSERT INTO admin (id, ruolo)
SELECT u.id, 'SUPER_ADMIN'
FROM utente u
WHERE u.email = 'admin@eurcine.it'
  AND NOT EXISTS (
    SELECT 1 FROM admin a WHERE a.id = u.id
  );
