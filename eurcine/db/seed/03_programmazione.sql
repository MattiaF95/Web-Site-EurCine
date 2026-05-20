-- Seed programmazione giornaliera (dati esempio da dev.md)
-- Data esempio scelta: 2026-05-20
-- Prezzi: 4.90 fino alle 18:00, 7.90 dopo le 18:00 (campi base pre/post in tabella)

INSERT INTO programmazione (film_id, sala_id, start_at, prezzo_base_pre18, prezzo_base_post18)
SELECT f.id, s.id, '2026-05-20 16:00:00', 4.90, 7.90
FROM film f, sala s
WHERE f.titolo = 'Vita privata' AND s.nome = 'Eurcine 7';

INSERT INTO programmazione (film_id, sala_id, start_at, prezzo_base_pre18, prezzo_base_post18)
SELECT f.id, s.id, '2026-05-20 16:45:00', 4.90, 7.90
FROM film f, sala s
WHERE f.titolo = 'Primavera' AND s.nome = 'Eurcine 5';

INSERT INTO programmazione (film_id, sala_id, start_at, prezzo_base_pre18, prezzo_base_post18)
SELECT f.id, s.id, '2026-05-20 19:30:00', 4.90, 7.90
FROM film f, sala s
WHERE f.titolo = 'Primavera' AND s.nome = 'Eurcine 6';

INSERT INTO programmazione (film_id, sala_id, start_at, prezzo_base_pre18, prezzo_base_post18)
SELECT f.id, s.id, '2026-05-20 15:45:00', 4.90, 7.90
FROM film f, sala s
WHERE f.titolo = 'Norimberga' AND s.nome = 'Eurcine 4';

INSERT INTO programmazione (film_id, sala_id, start_at, prezzo_base_pre18, prezzo_base_post18)
SELECT f.id, s.id, '2026-05-20 18:35:00', 4.90, 7.90
FROM film f, sala s
WHERE f.titolo = 'Norimberga' AND s.nome = 'Eurcine 7';

INSERT INTO programmazione (film_id, sala_id, start_at, prezzo_base_pre18, prezzo_base_post18)
SELECT f.id, s.id, '2026-05-20 21:15:00', 4.90, 7.90
FROM film f, sala s
WHERE f.titolo = 'Norimberga' AND s.nome = 'Eurcine 7';

INSERT INTO programmazione (film_id, sala_id, start_at, prezzo_base_pre18, prezzo_base_post18)
SELECT f.id, s.id, '2026-05-20 16:00:00', 4.90, 7.90
FROM film f, sala s
WHERE f.titolo = 'Buen camino' AND s.nome = 'Eurcine 1';

INSERT INTO programmazione (film_id, sala_id, start_at, prezzo_base_pre18, prezzo_base_post18)
SELECT f.id, s.id, '2026-05-20 18:00:00', 4.90, 7.90
FROM film f, sala s
WHERE f.titolo = 'Buen camino' AND s.nome = 'Eurcine 2';

INSERT INTO programmazione (film_id, sala_id, start_at, prezzo_base_pre18, prezzo_base_post18)
SELECT f.id, s.id, '2026-05-20 19:45:00', 4.90, 7.90
FROM film f, sala s
WHERE f.titolo = 'Buen camino' AND s.nome = 'Eurcine 2';

INSERT INTO programmazione (film_id, sala_id, start_at, prezzo_base_pre18, prezzo_base_post18)
SELECT f.id, s.id, '2026-05-20 21:30:00', 4.90, 7.90
FROM film f, sala s
WHERE f.titolo = 'Buen camino' AND s.nome = 'Eurcine 3';

INSERT INTO programmazione (film_id, sala_id, start_at, prezzo_base_pre18, prezzo_base_post18)
SELECT f.id, s.id, '2026-05-20 15:30:00', 4.90, 7.90
FROM film f, sala s
WHERE f.titolo = 'La grazia' AND s.nome = 'Eurcine 2';

INSERT INTO programmazione (film_id, sala_id, start_at, prezzo_base_pre18, prezzo_base_post18)
SELECT f.id, s.id, '2026-05-20 16:30:00', 4.90, 7.90
FROM film f, sala s
WHERE f.titolo = 'La grazia' AND s.nome = 'Eurcine 3';

INSERT INTO programmazione (film_id, sala_id, start_at, prezzo_base_pre18, prezzo_base_post18)
SELECT f.id, s.id, '2026-05-20 18:00:00', 4.90, 7.90
FROM film f, sala s
WHERE f.titolo = 'La grazia' AND s.nome = 'Eurcine 1';

INSERT INTO programmazione (film_id, sala_id, start_at, prezzo_base_pre18, prezzo_base_post18)
SELECT f.id, s.id, '2026-05-20 19:00:00', 4.90, 7.90
FROM film f, sala s
WHERE f.titolo = 'La grazia' AND s.nome = 'Eurcine 3';

INSERT INTO programmazione (film_id, sala_id, start_at, prezzo_base_pre18, prezzo_base_post18)
SELECT f.id, s.id, '2026-05-20 20:30:00', 4.90, 7.90
FROM film f, sala s
WHERE f.titolo = 'La grazia' AND s.nome = 'Eurcine 1';

INSERT INTO programmazione (film_id, sala_id, start_at, prezzo_base_pre18, prezzo_base_post18)
SELECT f.id, s.id, '2026-05-20 21:30:00', 4.90, 7.90
FROM film f, sala s
WHERE f.titolo = 'La grazia' AND s.nome = 'Eurcine 2';

INSERT INTO programmazione (film_id, sala_id, start_at, prezzo_base_pre18, prezzo_base_post18)
SELECT f.id, s.id, '2026-05-20 18:30:00', 4.90, 7.90
FROM film f, sala s
WHERE f.titolo = 'No other choice' AND s.nome = 'Eurcine 4';

INSERT INTO programmazione (film_id, sala_id, start_at, prezzo_base_pre18, prezzo_base_post18)
SELECT f.id, s.id, '2026-05-20 21:30:00', 4.90, 7.90
FROM film f, sala s
WHERE f.titolo = 'No other choice' AND s.nome = 'Eurcine 6';

INSERT INTO programmazione (film_id, sala_id, start_at, prezzo_base_pre18, prezzo_base_post18)
SELECT f.id, s.id, '2026-05-20 19:00:00', 4.90, 7.90
FROM film f, sala s
WHERE f.titolo = 'Una di famiglia' AND s.nome = 'Eurcine 5';

INSERT INTO programmazione (film_id, sala_id, start_at, prezzo_base_pre18, prezzo_base_post18)
SELECT f.id, s.id, '2026-05-20 21:30:00', 4.90, 7.90
FROM film f, sala s
WHERE f.titolo = 'Una di famiglia' AND s.nome = 'Eurcine 5';

INSERT INTO programmazione (film_id, sala_id, start_at, prezzo_base_pre18, prezzo_base_post18)
SELECT f.id, s.id, '2026-05-20 15:15:00', 4.90, 7.90
FROM film f, sala s
WHERE f.titolo = 'La piccola Amelie' AND s.nome = 'Eurcine 5';

INSERT INTO programmazione (film_id, sala_id, start_at, prezzo_base_pre18, prezzo_base_post18)
SELECT f.id, s.id, '2026-05-20 16:15:00', 4.90, 7.90
FROM film f, sala s
WHERE f.titolo = 'Inland empire' AND s.nome = 'Eurcine 6';

INSERT INTO programmazione (film_id, sala_id, start_at, prezzo_base_pre18, prezzo_base_post18)
SELECT f.id, s.id, '2026-05-20 21:15:00', 4.90, 7.90
FROM film f, sala s
WHERE f.titolo = 'Inland empire' AND s.nome = 'Eurcine 4';
