-- Seed sale + file + posti
-- Nota: i dati "esempio" in dev.md hanno alcune incongruenze; qui sono resi coerenti con i totali posti.

-- Sale
INSERT INTO sala (nome, descrizione)
SELECT 'Eurcine 1', 'Sala premium orientata a spettacoli serali ad alta qualita audio-video.'
WHERE NOT EXISTS (SELECT 1 FROM sala WHERE nome = 'Eurcine 1');
INSERT INTO sala (nome, descrizione)
SELECT 'Eurcine 2', 'Sala ampia e polivalente per blockbuster e rassegne del weekend.'
WHERE NOT EXISTS (SELECT 1 FROM sala WHERE nome = 'Eurcine 2');
INSERT INTO sala (nome, descrizione)
SELECT 'Eurcine 3', 'Sala immersiva con impostazione orientata a grandi eventi cinematografici.'
WHERE NOT EXISTS (SELECT 1 FROM sala WHERE nome = 'Eurcine 3');
INSERT INTO sala (nome, descrizione)
SELECT 'Eurcine 4', 'Sala dinamica per esperienze sensoriali e spettacoli evento.'
WHERE NOT EXISTS (SELECT 1 FROM sala WHERE nome = 'Eurcine 4');
INSERT INTO sala (nome, descrizione)
SELECT 'Eurcine 5', 'Sala family-friendly con comfort elevato e servizi accessibili.'
WHERE NOT EXISTS (SELECT 1 FROM sala WHERE nome = 'Eurcine 5');
INSERT INTO sala (nome, descrizione)
SELECT 'Eurcine 6', 'Sala compatta con focus su acustica controllata e tecnologia moderna.'
WHERE NOT EXISTS (SELECT 1 FROM sala WHERE nome = 'Eurcine 6');
INSERT INTO sala (nome, descrizione)
SELECT 'Eurcine 7', 'Sala versatile per rassegne, anteprime e lunghe maratone.'
WHERE NOT EXISTS (SELECT 1 FROM sala WHERE nome = 'Eurcine 7');

-- Catalogo caratteristiche sala
INSERT INTO caratteristica_sala (categoria, caratteristica)
SELECT 'Video', 'Proiezione Laser 4K'
WHERE NOT EXISTS (SELECT 1 FROM caratteristica_sala WHERE categoria = 'Video' AND caratteristica = 'Proiezione Laser 4K');
INSERT INTO caratteristica_sala (categoria, caratteristica)
SELECT 'Video', 'Proiezione Laser RGB'
WHERE NOT EXISTS (SELECT 1 FROM caratteristica_sala WHERE categoria = 'Video' AND caratteristica = 'Proiezione Laser RGB');
INSERT INTO caratteristica_sala (categoria, caratteristica)
SELECT 'Video', 'Schermo IMAX'
WHERE NOT EXISTS (SELECT 1 FROM caratteristica_sala WHERE categoria = 'Video' AND caratteristica = 'Schermo IMAX');
INSERT INTO caratteristica_sala (categoria, caratteristica)
SELECT 'Video', 'Schermo curvo'
WHERE NOT EXISTS (SELECT 1 FROM caratteristica_sala WHERE categoria = 'Video' AND caratteristica = 'Schermo curvo');
INSERT INTO caratteristica_sala (categoria, caratteristica)
SELECT 'Video', 'Schermo Silver Screen (3D)'
WHERE NOT EXISTS (SELECT 1 FROM caratteristica_sala WHERE categoria = 'Video' AND caratteristica = 'Schermo Silver Screen (3D)');
INSERT INTO caratteristica_sala (categoria, caratteristica)
SELECT 'Video', 'Tecnologia HFR (High Frame Rate)'
WHERE NOT EXISTS (SELECT 1 FROM caratteristica_sala WHERE categoria = 'Video' AND caratteristica = 'Tecnologia HFR (High Frame Rate)');
INSERT INTO caratteristica_sala (categoria, caratteristica)
SELECT 'Video', 'Tecnologia ScreenX (270 gradi)'
WHERE NOT EXISTS (SELECT 1 FROM caratteristica_sala WHERE categoria = 'Video' AND caratteristica = 'Tecnologia ScreenX (270 gradi)');

INSERT INTO caratteristica_sala (categoria, caratteristica)
SELECT 'Audio', 'Dolby Atmos'
WHERE NOT EXISTS (SELECT 1 FROM caratteristica_sala WHERE categoria = 'Audio' AND caratteristica = 'Dolby Atmos');
INSERT INTO caratteristica_sala (categoria, caratteristica)
SELECT 'Audio', 'DTS:X'
WHERE NOT EXISTS (SELECT 1 FROM caratteristica_sala WHERE categoria = 'Audio' AND caratteristica = 'DTS:X');
INSERT INTO caratteristica_sala (categoria, caratteristica)
SELECT 'Audio', 'Auro 3D'
WHERE NOT EXISTS (SELECT 1 FROM caratteristica_sala WHERE categoria = 'Audio' AND caratteristica = 'Auro 3D');
INSERT INTO caratteristica_sala (categoria, caratteristica)
SELECT 'Audio', 'Surround 7.1'
WHERE NOT EXISTS (SELECT 1 FROM caratteristica_sala WHERE categoria = 'Audio' AND caratteristica = 'Surround 7.1');
INSERT INTO caratteristica_sala (categoria, caratteristica)
SELECT 'Audio', 'Surround 5.1'
WHERE NOT EXISTS (SELECT 1 FROM caratteristica_sala WHERE categoria = 'Audio' AND caratteristica = 'Surround 5.1');
INSERT INTO caratteristica_sala (categoria, caratteristica)
SELECT 'Audio', 'Certificazione THX'
WHERE NOT EXISTS (SELECT 1 FROM caratteristica_sala WHERE categoria = 'Audio' AND caratteristica = 'Certificazione THX');

INSERT INTO caratteristica_sala (categoria, caratteristica)
SELECT 'Comfort e Sedute', 'Poltrone VIP Recliner (elettriche)'
WHERE NOT EXISTS (SELECT 1 FROM caratteristica_sala WHERE categoria = 'Comfort e Sedute' AND caratteristica = 'Poltrone VIP Recliner (elettriche)');
INSERT INTO caratteristica_sala (categoria, caratteristica)
SELECT 'Comfort e Sedute', 'Love Seats (divanetti per coppie)'
WHERE NOT EXISTS (SELECT 1 FROM caratteristica_sala WHERE categoria = 'Comfort e Sedute' AND caratteristica = 'Love Seats (divanetti per coppie)');
INSERT INTO caratteristica_sala (categoria, caratteristica)
SELECT 'Comfort e Sedute', 'Disposizione Stadium Seating (gradinata)'
WHERE NOT EXISTS (SELECT 1 FROM caratteristica_sala WHERE categoria = 'Comfort e Sedute' AND caratteristica = 'Disposizione Stadium Seating (gradinata)');
INSERT INTO caratteristica_sala (categoria, caratteristica)
SELECT 'Comfort e Sedute', 'Sedili riscaldati'
WHERE NOT EXISTS (SELECT 1 FROM caratteristica_sala WHERE categoria = 'Comfort e Sedute' AND caratteristica = 'Sedili riscaldati');
INSERT INTO caratteristica_sala (categoria, caratteristica)
SELECT 'Comfort e Sedute', 'Porte USB di ricarica'
WHERE NOT EXISTS (SELECT 1 FROM caratteristica_sala WHERE categoria = 'Comfort e Sedute' AND caratteristica = 'Porte USB di ricarica');

INSERT INTO caratteristica_sala (categoria, caratteristica)
SELECT 'Effetti Dinamici', 'Sedili mobili 4DX'
WHERE NOT EXISTS (SELECT 1 FROM caratteristica_sala WHERE categoria = 'Effetti Dinamici' AND caratteristica = 'Sedili mobili 4DX');
INSERT INTO caratteristica_sala (categoria, caratteristica)
SELECT 'Effetti Dinamici', 'Sedili vibranti D-Box'
WHERE NOT EXISTS (SELECT 1 FROM caratteristica_sala WHERE categoria = 'Effetti Dinamici' AND caratteristica = 'Sedili vibranti D-Box');
INSERT INTO caratteristica_sala (categoria, caratteristica)
SELECT 'Effetti Dinamici', 'Effetti vento in sala'
WHERE NOT EXISTS (SELECT 1 FROM caratteristica_sala WHERE categoria = 'Effetti Dinamici' AND caratteristica = 'Effetti vento in sala');

INSERT INTO caratteristica_sala (categoria, caratteristica)
SELECT 'Servizi e Accessibilita', 'Ordinazione cibo via App al posto'
WHERE NOT EXISTS (SELECT 1 FROM caratteristica_sala WHERE categoria = 'Servizi e Accessibilita' AND caratteristica = 'Ordinazione cibo via App al posto');
INSERT INTO caratteristica_sala (categoria, caratteristica)
SELECT 'Servizi e Accessibilita', 'Posti dedicati disabilita motorie'
WHERE NOT EXISTS (SELECT 1 FROM caratteristica_sala WHERE categoria = 'Servizi e Accessibilita' AND caratteristica = 'Posti dedicati disabilita motorie');
INSERT INTO caratteristica_sala (categoria, caratteristica)
SELECT 'Servizi e Accessibilita', 'Cuffie per audiodescrizione'
WHERE NOT EXISTS (SELECT 1 FROM caratteristica_sala WHERE categoria = 'Servizi e Accessibilita' AND caratteristica = 'Cuffie per audiodescrizione');
INSERT INTO caratteristica_sala (categoria, caratteristica)
SELECT 'Servizi e Accessibilita', 'Dispositivi sottotitoli per non udenti'
WHERE NOT EXISTS (SELECT 1 FROM caratteristica_sala WHERE categoria = 'Servizi e Accessibilita' AND caratteristica = 'Dispositivi sottotitoli per non udenti');

-- Assegnazione caratteristiche alle sale (1 sola voce Video e 1 sola voce Audio per sala; categorie multiple consentite)
INSERT INTO sala_caratteristica (sala_id, caratteristica_sala_id)
SELECT s.id, c.id
FROM sala s
JOIN (
  SELECT 'Eurcine 1' AS sala, 'Video' AS categoria, 'Proiezione Laser 4K' AS caratteristica UNION ALL
  SELECT 'Eurcine 1', 'Audio', 'Dolby Atmos' UNION ALL
  SELECT 'Eurcine 1', 'Comfort e Sedute', 'Poltrone VIP Recliner (elettriche)' UNION ALL
  SELECT 'Eurcine 1', 'Comfort e Sedute', 'Porte USB di ricarica' UNION ALL
  SELECT 'Eurcine 1', 'Servizi e Accessibilita', 'Ordinazione cibo via App al posto' UNION ALL

  SELECT 'Eurcine 2', 'Video', 'Proiezione Laser RGB' UNION ALL
  SELECT 'Eurcine 2', 'Audio', 'DTS:X' UNION ALL
  SELECT 'Eurcine 2', 'Comfort e Sedute', 'Disposizione Stadium Seating (gradinata)' UNION ALL
  SELECT 'Eurcine 2', 'Comfort e Sedute', 'Sedili riscaldati' UNION ALL
  SELECT 'Eurcine 2', 'Servizi e Accessibilita', 'Cuffie per audiodescrizione' UNION ALL

  SELECT 'Eurcine 3', 'Video', 'Schermo IMAX' UNION ALL
  SELECT 'Eurcine 3', 'Audio', 'Dolby Atmos' UNION ALL
  SELECT 'Eurcine 3', 'Comfort e Sedute', 'Disposizione Stadium Seating (gradinata)' UNION ALL
  SELECT 'Eurcine 3', 'Servizi e Accessibilita', 'Dispositivi sottotitoli per non udenti' UNION ALL

  SELECT 'Eurcine 4', 'Video', 'Tecnologia ScreenX (270 gradi)' UNION ALL
  SELECT 'Eurcine 4', 'Audio', 'Auro 3D' UNION ALL
  SELECT 'Eurcine 4', 'Effetti Dinamici', 'Sedili mobili 4DX' UNION ALL
  SELECT 'Eurcine 4', 'Effetti Dinamici', 'Effetti vento in sala' UNION ALL
  SELECT 'Eurcine 4', 'Comfort e Sedute', 'Poltrone VIP Recliner (elettriche)' UNION ALL

  SELECT 'Eurcine 5', 'Video', 'Schermo Silver Screen (3D)' UNION ALL
  SELECT 'Eurcine 5', 'Audio', 'Surround 5.1' UNION ALL
  SELECT 'Eurcine 5', 'Comfort e Sedute', 'Love Seats (divanetti per coppie)' UNION ALL
  SELECT 'Eurcine 5', 'Comfort e Sedute', 'Porte USB di ricarica' UNION ALL
  SELECT 'Eurcine 5', 'Servizi e Accessibilita', 'Posti dedicati disabilita motorie' UNION ALL

  SELECT 'Eurcine 6', 'Video', 'Tecnologia HFR (High Frame Rate)' UNION ALL
  SELECT 'Eurcine 6', 'Audio', 'DTS:X' UNION ALL
  SELECT 'Eurcine 6', 'Effetti Dinamici', 'Sedili vibranti D-Box' UNION ALL
  SELECT 'Eurcine 6', 'Servizi e Accessibilita', 'Cuffie per audiodescrizione' UNION ALL

  SELECT 'Eurcine 7', 'Video', 'Schermo curvo' UNION ALL
  SELECT 'Eurcine 7', 'Audio', 'Dolby Atmos' UNION ALL
  SELECT 'Eurcine 7', 'Comfort e Sedute', 'Disposizione Stadium Seating (gradinata)' UNION ALL
  SELECT 'Eurcine 7', 'Comfort e Sedute', 'Love Seats (divanetti per coppie)' UNION ALL
  SELECT 'Eurcine 7', 'Servizi e Accessibilita', 'Dispositivi sottotitoli per non udenti'
) x ON x.sala = s.nome
JOIN caratteristica_sala c ON c.categoria = x.categoria AND c.caratteristica = x.caratteristica
WHERE NOT EXISTS (
  SELECT 1
  FROM sala_caratteristica sc
  WHERE sc.sala_id = s.id
    AND sc.caratteristica_sala_id = c.id
);

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
