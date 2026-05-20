-- Seed film + lookup (lingua, genere) + relazione film_genere

-- Lingue
INSERT INTO lingua (nome)
SELECT 'Italiano' WHERE NOT EXISTS (SELECT 1 FROM lingua WHERE nome = 'Italiano');
INSERT INTO lingua (nome)
SELECT 'Inglese' WHERE NOT EXISTS (SELECT 1 FROM lingua WHERE nome = 'Inglese');
INSERT INTO lingua (nome)
SELECT 'Francese' WHERE NOT EXISTS (SELECT 1 FROM lingua WHERE nome = 'Francese');

-- Generi
INSERT INTO genere (nome)
SELECT 'Dramma' WHERE NOT EXISTS (SELECT 1 FROM genere WHERE nome = 'Dramma');
INSERT INTO genere (nome)
SELECT 'Mistero' WHERE NOT EXISTS (SELECT 1 FROM genere WHERE nome = 'Mistero');
INSERT INTO genere (nome)
SELECT 'Crime' WHERE NOT EXISTS (SELECT 1 FROM genere WHERE nome = 'Crime');
INSERT INTO genere (nome)
SELECT 'Musica' WHERE NOT EXISTS (SELECT 1 FROM genere WHERE nome = 'Musica');
INSERT INTO genere (nome)
SELECT 'Storia' WHERE NOT EXISTS (SELECT 1 FROM genere WHERE nome = 'Storia');
INSERT INTO genere (nome)
SELECT 'Commedia' WHERE NOT EXISTS (SELECT 1 FROM genere WHERE nome = 'Commedia');
INSERT INTO genere (nome)
SELECT 'Thriller' WHERE NOT EXISTS (SELECT 1 FROM genere WHERE nome = 'Thriller');
INSERT INTO genere (nome)
SELECT 'Animazione' WHERE NOT EXISTS (SELECT 1 FROM genere WHERE nome = 'Animazione');
INSERT INTO genere (nome)
SELECT 'Famiglia' WHERE NOT EXISTS (SELECT 1 FROM genere WHERE nome = 'Famiglia');
INSERT INTO genere (nome)
SELECT 'Horror' WHERE NOT EXISTS (SELECT 1 FROM genere WHERE nome = 'Horror');
INSERT INTO genere (nome)
SELECT 'Fantasy' WHERE NOT EXISTS (SELECT 1 FROM genere WHERE nome = 'Fantasy');

-- Film
INSERT INTO film (titolo, durata_min, lingua_id)
SELECT 'Vita privata', 105, l.id
FROM lingua l
WHERE l.nome = 'Italiano'
  AND NOT EXISTS (SELECT 1 FROM film f WHERE f.titolo = 'Vita privata');

INSERT INTO film (titolo, durata_min, lingua_id)
SELECT 'Primavera', 120, l.id
FROM lingua l
WHERE l.nome = 'Italiano'
  AND NOT EXISTS (SELECT 1 FROM film f WHERE f.titolo = 'Primavera');

INSERT INTO film (titolo, durata_min, lingua_id)
SELECT 'Norimberga', 148, l.id
FROM lingua l
WHERE l.nome = 'Italiano'
  AND NOT EXISTS (SELECT 1 FROM film f WHERE f.titolo = 'Norimberga');

INSERT INTO film (titolo, durata_min, lingua_id)
SELECT 'Buen camino', 90, l.id
FROM lingua l
WHERE l.nome = 'Italiano'
  AND NOT EXISTS (SELECT 1 FROM film f WHERE f.titolo = 'Buen camino');

INSERT INTO film (titolo, durata_min, lingua_id)
SELECT 'La grazia', 133, l.id
FROM lingua l
WHERE l.nome = 'Italiano'
  AND NOT EXISTS (SELECT 1 FROM film f WHERE f.titolo = 'La grazia');

INSERT INTO film (titolo, durata_min, lingua_id)
SELECT 'No other choice', 139, l.id
FROM lingua l
WHERE l.nome = 'Inglese'
  AND NOT EXISTS (SELECT 1 FROM film f WHERE f.titolo = 'No other choice');

INSERT INTO film (titolo, durata_min, lingua_id)
SELECT 'Una di famiglia', 131, l.id
FROM lingua l
WHERE l.nome = 'Italiano'
  AND NOT EXISTS (SELECT 1 FROM film f WHERE f.titolo = 'Una di famiglia');

INSERT INTO film (titolo, durata_min, lingua_id)
SELECT 'La piccola Amelie', 77, l.id
FROM lingua l
WHERE l.nome = 'Francese'
  AND NOT EXISTS (SELECT 1 FROM film f WHERE f.titolo = 'La piccola Amelie');

INSERT INTO film (titolo, durata_min, lingua_id)
SELECT 'Inland empire', 180, l.id
FROM lingua l
WHERE l.nome = 'Inglese'
  AND NOT EXISTS (SELECT 1 FROM film f WHERE f.titolo = 'Inland empire');

INSERT INTO film (titolo, durata_min, lingua_id)
SELECT 'Marty supreme', 150, l.id
FROM lingua l
WHERE l.nome = 'Inglese'
  AND NOT EXISTS (SELECT 1 FROM film f WHERE f.titolo = 'Marty supreme');

INSERT INTO film (titolo, durata_min, lingua_id)
SELECT 'Sentimental value', 135, l.id
FROM lingua l
WHERE l.nome = 'Italiano'
  AND NOT EXISTS (SELECT 1 FROM film f WHERE f.titolo = 'Sentimental value');

-- Relazioni film-genere
-- Vita privata
INSERT INTO film_genere (film_id, genere_id)
SELECT f.id, g.id FROM film f, genere g
WHERE f.titolo = 'Vita privata' AND g.nome = 'Dramma'
  AND NOT EXISTS (
    SELECT 1 FROM film_genere fg WHERE fg.film_id = f.id AND fg.genere_id = g.id
  );
INSERT INTO film_genere (film_id, genere_id)
SELECT f.id, g.id FROM film f, genere g
WHERE f.titolo = 'Vita privata' AND g.nome = 'Mistero'
  AND NOT EXISTS (SELECT 1 FROM film_genere fg WHERE fg.film_id = f.id AND fg.genere_id = g.id);
INSERT INTO film_genere (film_id, genere_id)
SELECT f.id, g.id FROM film f, genere g
WHERE f.titolo = 'Vita privata' AND g.nome = 'Crime'
  AND NOT EXISTS (SELECT 1 FROM film_genere fg WHERE fg.film_id = f.id AND fg.genere_id = g.id);

-- Primavera
INSERT INTO film_genere (film_id, genere_id)
SELECT f.id, g.id FROM film f, genere g
WHERE f.titolo = 'Primavera' AND g.nome = 'Musica'
  AND NOT EXISTS (SELECT 1 FROM film_genere fg WHERE fg.film_id = f.id AND fg.genere_id = g.id);
INSERT INTO film_genere (film_id, genere_id)
SELECT f.id, g.id FROM film f, genere g
WHERE f.titolo = 'Primavera' AND g.nome = 'Dramma'
  AND NOT EXISTS (SELECT 1 FROM film_genere fg WHERE fg.film_id = f.id AND fg.genere_id = g.id);
INSERT INTO film_genere (film_id, genere_id)
SELECT f.id, g.id FROM film f, genere g
WHERE f.titolo = 'Primavera' AND g.nome = 'Storia'
  AND NOT EXISTS (SELECT 1 FROM film_genere fg WHERE fg.film_id = f.id AND fg.genere_id = g.id);

-- Norimberga
INSERT INTO film_genere (film_id, genere_id)
SELECT f.id, g.id FROM film f, genere g
WHERE f.titolo = 'Norimberga' AND g.nome IN ('Dramma', 'Storia')
  AND NOT EXISTS (SELECT 1 FROM film_genere fg WHERE fg.film_id = f.id AND fg.genere_id = g.id);

-- Buen camino
INSERT INTO film_genere (film_id, genere_id)
SELECT f.id, g.id FROM film f, genere g
WHERE f.titolo = 'Buen camino' AND g.nome = 'Commedia'
  AND NOT EXISTS (SELECT 1 FROM film_genere fg WHERE fg.film_id = f.id AND fg.genere_id = g.id);

-- La grazia
INSERT INTO film_genere (film_id, genere_id)
SELECT f.id, g.id FROM film f, genere g
WHERE f.titolo = 'La grazia' AND g.nome IN ('Commedia', 'Dramma')
  AND NOT EXISTS (SELECT 1 FROM film_genere fg WHERE fg.film_id = f.id AND fg.genere_id = g.id);

-- No other choice
INSERT INTO film_genere (film_id, genere_id)
SELECT f.id, g.id FROM film f, genere g
WHERE f.titolo = 'No other choice' AND g.nome IN ('Commedia', 'Thriller', 'Crime')
  AND NOT EXISTS (SELECT 1 FROM film_genere fg WHERE fg.film_id = f.id AND fg.genere_id = g.id);

-- Una di famiglia
INSERT INTO film_genere (film_id, genere_id)
SELECT f.id, g.id FROM film f, genere g
WHERE f.titolo = 'Una di famiglia' AND g.nome IN ('Thriller', 'Mistero')
  AND NOT EXISTS (SELECT 1 FROM film_genere fg WHERE fg.film_id = f.id AND fg.genere_id = g.id);

-- La piccola Amelie
INSERT INTO film_genere (film_id, genere_id)
SELECT f.id, g.id FROM film f, genere g
WHERE f.titolo = 'La piccola Amelie' AND g.nome IN ('Animazione', 'Dramma', 'Famiglia')
  AND NOT EXISTS (SELECT 1 FROM film_genere fg WHERE fg.film_id = f.id AND fg.genere_id = g.id);

-- Inland empire
INSERT INTO film_genere (film_id, genere_id)
SELECT f.id, g.id FROM film f, genere g
WHERE f.titolo = 'Inland empire' AND g.nome IN ('Horror', 'Thriller', 'Fantasy', 'Dramma', 'Mistero')
  AND NOT EXISTS (SELECT 1 FROM film_genere fg WHERE fg.film_id = f.id AND fg.genere_id = g.id);

-- Marty supreme
INSERT INTO film_genere (film_id, genere_id)
SELECT f.id, g.id FROM film f, genere g
WHERE f.titolo = 'Marty supreme' AND g.nome IN ('Commedia', 'Dramma')
  AND NOT EXISTS (SELECT 1 FROM film_genere fg WHERE fg.film_id = f.id AND fg.genere_id = g.id);

-- Sentimental value
INSERT INTO film_genere (film_id, genere_id)
SELECT f.id, g.id FROM film f, genere g
WHERE f.titolo = 'Sentimental value' AND g.nome = 'Dramma'
  AND NOT EXISTS (SELECT 1 FROM film_genere fg WHERE fg.film_id = f.id AND fg.genere_id = g.id);
