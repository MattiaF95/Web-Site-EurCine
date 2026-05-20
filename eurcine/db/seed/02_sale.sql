-- Seed sale + file + posti
-- Nota: i dati "esempio" in dev.md hanno alcune incongruenze; qui sono resi coerenti con i totali posti.

-- Sale
INSERT INTO sala (nome)
SELECT 'Eurcine 1' WHERE NOT EXISTS (SELECT 1 FROM sala WHERE nome = 'Eurcine 1');
INSERT INTO sala (nome)
SELECT 'Eurcine 2' WHERE NOT EXISTS (SELECT 1 FROM sala WHERE nome = 'Eurcine 2');
INSERT INTO sala (nome)
SELECT 'Eurcine 3' WHERE NOT EXISTS (SELECT 1 FROM sala WHERE nome = 'Eurcine 3');
INSERT INTO sala (nome)
SELECT 'Eurcine 4' WHERE NOT EXISTS (SELECT 1 FROM sala WHERE nome = 'Eurcine 4');
INSERT INTO sala (nome)
SELECT 'Eurcine 5' WHERE NOT EXISTS (SELECT 1 FROM sala WHERE nome = 'Eurcine 5');
INSERT INTO sala (nome)
SELECT 'Eurcine 6' WHERE NOT EXISTS (SELECT 1 FROM sala WHERE nome = 'Eurcine 6');
INSERT INTO sala (nome)
SELECT 'Eurcine 7' WHERE NOT EXISTS (SELECT 1 FROM sala WHERE nome = 'Eurcine 7');

-- File (righe)
-- Eurcine 1: 11 file (A-K)
INSERT INTO fila (sala_id, lettera)
SELECT s.id, x.lettera
FROM sala s
JOIN (
  SELECT 'A' AS lettera UNION SELECT 'B' UNION SELECT 'C' UNION SELECT 'D' UNION SELECT 'E'
  UNION SELECT 'F' UNION SELECT 'G' UNION SELECT 'H' UNION SELECT 'I' UNION SELECT 'J' UNION SELECT 'K'
) x
WHERE s.nome = 'Eurcine 1'
  AND NOT EXISTS (SELECT 1 FROM fila f WHERE f.sala_id = s.id AND f.lettera = x.lettera);

-- Eurcine 2: 14 file (A-N)
INSERT INTO fila (sala_id, lettera)
SELECT s.id, x.lettera
FROM sala s
JOIN (
  SELECT 'A' AS lettera UNION SELECT 'B' UNION SELECT 'C' UNION SELECT 'D' UNION SELECT 'E' UNION SELECT 'F' UNION SELECT 'G'
  UNION SELECT 'H' UNION SELECT 'I' UNION SELECT 'J' UNION SELECT 'K' UNION SELECT 'L' UNION SELECT 'M' UNION SELECT 'N'
) x
WHERE s.nome = 'Eurcine 2'
  AND NOT EXISTS (SELECT 1 FROM fila f WHERE f.sala_id = s.id AND f.lettera = x.lettera);

-- Eurcine 3: 10 file (A-J)
INSERT INTO fila (sala_id, lettera)
SELECT s.id, x.lettera
FROM sala s
JOIN (
  SELECT 'A' AS lettera UNION SELECT 'B' UNION SELECT 'C' UNION SELECT 'D' UNION SELECT 'E'
  UNION SELECT 'F' UNION SELECT 'G' UNION SELECT 'H' UNION SELECT 'I' UNION SELECT 'J'
) x
WHERE s.nome = 'Eurcine 3'
  AND NOT EXISTS (SELECT 1 FROM fila f WHERE f.sala_id = s.id AND f.lettera = x.lettera);

-- Eurcine 4: 6 file (A-F)
INSERT INTO fila (sala_id, lettera)
SELECT s.id, x.lettera
FROM sala s
JOIN (SELECT 'A' AS lettera UNION SELECT 'B' UNION SELECT 'C' UNION SELECT 'D' UNION SELECT 'E' UNION SELECT 'F') x
WHERE s.nome = 'Eurcine 4'
  AND NOT EXISTS (SELECT 1 FROM fila f WHERE f.sala_id = s.id AND f.lettera = x.lettera);

-- Eurcine 5: 8 file (A-H)
INSERT INTO fila (sala_id, lettera)
SELECT s.id, x.lettera
FROM sala s
JOIN (
  SELECT 'A' AS lettera UNION SELECT 'B' UNION SELECT 'C' UNION SELECT 'D'
  UNION SELECT 'E' UNION SELECT 'F' UNION SELECT 'G' UNION SELECT 'H'
) x
WHERE s.nome = 'Eurcine 5'
  AND NOT EXISTS (SELECT 1 FROM fila f WHERE f.sala_id = s.id AND f.lettera = x.lettera);

-- Eurcine 6: 6 file (A-F)
INSERT INTO fila (sala_id, lettera)
SELECT s.id, x.lettera
FROM sala s
JOIN (SELECT 'A' AS lettera UNION SELECT 'B' UNION SELECT 'C' UNION SELECT 'D' UNION SELECT 'E' UNION SELECT 'F') x
WHERE s.nome = 'Eurcine 6'
  AND NOT EXISTS (SELECT 1 FROM fila f WHERE f.sala_id = s.id AND f.lettera = x.lettera);

-- Eurcine 7: 10 file (A-J)
INSERT INTO fila (sala_id, lettera)
SELECT s.id, x.lettera
FROM sala s
JOIN (
  SELECT 'A' AS lettera UNION SELECT 'B' UNION SELECT 'C' UNION SELECT 'D' UNION SELECT 'E'
  UNION SELECT 'F' UNION SELECT 'G' UNION SELECT 'H' UNION SELECT 'I' UNION SELECT 'J'
) x
WHERE s.nome = 'Eurcine 7'
  AND NOT EXISTS (SELECT 1 FROM fila f WHERE f.sala_id = s.id AND f.lettera = x.lettera);

-- Posti per fila
-- Utility numeri 1..16
-- Eurcine 1: 16 posti per 11 file = 176
INSERT INTO posto (fila_id, numero, attivo)
SELECT f.id, n.num, true
FROM fila f
JOIN sala s ON s.id = f.sala_id
JOIN (
  SELECT 1 AS num UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8
  UNION SELECT 9 UNION SELECT 10 UNION SELECT 11 UNION SELECT 12 UNION SELECT 13 UNION SELECT 14 UNION SELECT 15 UNION SELECT 16
) n
WHERE s.nome = 'Eurcine 1'
  AND NOT EXISTS (SELECT 1 FROM posto p WHERE p.fila_id = f.id AND p.numero = n.num);

-- Eurcine 2: totale 184 su 14 file (A,B da 14 posti; C-N da 13 posti)
INSERT INTO posto (fila_id, numero, attivo)
SELECT f.id, n.num, true
FROM fila f
JOIN sala s ON s.id = f.sala_id
JOIN (
  SELECT 1 AS num UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7
  UNION SELECT 8 UNION SELECT 9 UNION SELECT 10 UNION SELECT 11 UNION SELECT 12 UNION SELECT 13 UNION SELECT 14
) n
WHERE s.nome = 'Eurcine 2'
  AND f.lettera IN ('A', 'B')
  AND NOT EXISTS (SELECT 1 FROM posto p WHERE p.fila_id = f.id AND p.numero = n.num);

INSERT INTO posto (fila_id, numero, attivo)
SELECT f.id, n.num, true
FROM fila f
JOIN sala s ON s.id = f.sala_id
JOIN (
  SELECT 1 AS num UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7
  UNION SELECT 8 UNION SELECT 9 UNION SELECT 10 UNION SELECT 11 UNION SELECT 12 UNION SELECT 13
) n
WHERE s.nome = 'Eurcine 2'
  AND f.lettera NOT IN ('A', 'B')
  AND NOT EXISTS (SELECT 1 FROM posto p WHERE p.fila_id = f.id AND p.numero = n.num);

-- Eurcine 3: 12 posti per 10 file = 120
INSERT INTO posto (fila_id, numero, attivo)
SELECT f.id, n.num, true
FROM fila f
JOIN sala s ON s.id = f.sala_id
JOIN (
  SELECT 1 AS num UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6
  UNION SELECT 7 UNION SELECT 8 UNION SELECT 9 UNION SELECT 10 UNION SELECT 11 UNION SELECT 12
) n
WHERE s.nome = 'Eurcine 3'
  AND NOT EXISTS (SELECT 1 FROM posto p WHERE p.fila_id = f.id AND p.numero = n.num);

-- Eurcine 4: 15 posti per 6 file = 90
INSERT INTO posto (fila_id, numero, attivo)
SELECT f.id, n.num, true
FROM fila f
JOIN sala s ON s.id = f.sala_id
JOIN (
  SELECT 1 AS num UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7
  UNION SELECT 8 UNION SELECT 9 UNION SELECT 10 UNION SELECT 11 UNION SELECT 12 UNION SELECT 13 UNION SELECT 14 UNION SELECT 15
) n
WHERE s.nome = 'Eurcine 4'
  AND NOT EXISTS (SELECT 1 FROM posto p WHERE p.fila_id = f.id AND p.numero = n.num);

-- Eurcine 5: 9 posti per 8 file = 72
INSERT INTO posto (fila_id, numero, attivo)
SELECT f.id, n.num, true
FROM fila f
JOIN sala s ON s.id = f.sala_id
JOIN (
  SELECT 1 AS num UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9
) n
WHERE s.nome = 'Eurcine 5'
  AND NOT EXISTS (SELECT 1 FROM posto p WHERE p.fila_id = f.id AND p.numero = n.num);

-- Eurcine 6: 9 posti per 6 file = 54
INSERT INTO posto (fila_id, numero, attivo)
SELECT f.id, n.num, true
FROM fila f
JOIN sala s ON s.id = f.sala_id
JOIN (
  SELECT 1 AS num UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9
) n
WHERE s.nome = 'Eurcine 6'
  AND NOT EXISTS (SELECT 1 FROM posto p WHERE p.fila_id = f.id AND p.numero = n.num);

-- Eurcine 7: 8 posti per 10 file = 80
INSERT INTO posto (fila_id, numero, attivo)
SELECT f.id, n.num, true
FROM fila f
JOIN sala s ON s.id = f.sala_id
JOIN (
  SELECT 1 AS num UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8
) n
WHERE s.nome = 'Eurcine 7'
  AND NOT EXISTS (SELECT 1 FROM posto p WHERE p.fila_id = f.id AND p.numero = n.num);
