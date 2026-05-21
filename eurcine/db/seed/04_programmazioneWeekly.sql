-- Seed programmazione settimanale (7 giorni, idempotente)
-- Settimana: 2026-06-01 -> 2026-06-07
-- Dal 4° giorno (2026-06-04) rimossi: Buen camino, La piccola Amelie, Sentimental value
-- Inseriti: Mortal Kombat II, Super Mario Galaxy, Star Wars - The Mandalorian and Grogu

INSERT INTO programmazione (film_id, sala_id, start_at, prezzo_base_pre18, prezzo_base_post18)
SELECT f.id, s.id, sch.start_at, 4.90, 7.90
FROM (
  -- Giorno 1
  SELECT 'Vita privata' AS titolo, 'Eurcine 1' AS sala, '2026-06-01 15:00:00' AS start_at UNION ALL
  SELECT 'Primavera', 'Eurcine 2', '2026-06-01 15:00:00' UNION ALL
  SELECT 'Norimberga', 'Eurcine 3', '2026-06-01 15:00:00' UNION ALL
  SELECT 'La grazia', 'Eurcine 4', '2026-06-01 15:00:00' UNION ALL
  SELECT 'No other choice', 'Eurcine 5', '2026-06-01 15:00:00' UNION ALL
  SELECT 'Una di famiglia', 'Eurcine 6', '2026-06-01 15:00:00' UNION ALL
  SELECT 'La piccola Amelie', 'Eurcine 7', '2026-06-01 15:00:00' UNION ALL
  SELECT 'Inland empire', 'Eurcine 1', '2026-06-01 20:00:00' UNION ALL
  SELECT 'Marty supreme', 'Eurcine 2', '2026-06-01 20:00:00' UNION ALL
  SELECT 'Sentimental value', 'Eurcine 3', '2026-06-01 20:00:00' UNION ALL
  SELECT 'Buen camino', 'Eurcine 4', '2026-06-01 20:00:00' UNION ALL
  SELECT 'Vita privata', 'Eurcine 5', '2026-06-01 20:00:00' UNION ALL
  SELECT 'Primavera', 'Eurcine 6', '2026-06-01 20:00:00' UNION ALL
  SELECT 'Norimberga', 'Eurcine 7', '2026-06-01 20:00:00' UNION ALL

  -- Giorno 2
  SELECT 'La grazia', 'Eurcine 1', '2026-06-02 15:00:00' UNION ALL
  SELECT 'No other choice', 'Eurcine 2', '2026-06-02 15:00:00' UNION ALL
  SELECT 'Una di famiglia', 'Eurcine 3', '2026-06-02 15:00:00' UNION ALL
  SELECT 'La piccola Amelie', 'Eurcine 4', '2026-06-02 15:00:00' UNION ALL
  SELECT 'Vita privata', 'Eurcine 5', '2026-06-02 15:00:00' UNION ALL
  SELECT 'Primavera', 'Eurcine 6', '2026-06-02 15:00:00' UNION ALL
  SELECT 'Norimberga', 'Eurcine 7', '2026-06-02 15:00:00' UNION ALL
  SELECT 'Buen camino', 'Eurcine 1', '2026-06-02 20:00:00' UNION ALL
  SELECT 'Sentimental value', 'Eurcine 2', '2026-06-02 20:00:00' UNION ALL
  SELECT 'Inland empire', 'Eurcine 3', '2026-06-02 20:00:00' UNION ALL
  SELECT 'Marty supreme', 'Eurcine 4', '2026-06-02 20:00:00' UNION ALL
  SELECT 'La grazia', 'Eurcine 5', '2026-06-02 20:00:00' UNION ALL
  SELECT 'No other choice', 'Eurcine 6', '2026-06-02 20:00:00' UNION ALL
  SELECT 'Una di famiglia', 'Eurcine 7', '2026-06-02 20:00:00' UNION ALL

  -- Giorno 3
  SELECT 'Primavera', 'Eurcine 1', '2026-06-03 15:00:00' UNION ALL
  SELECT 'Norimberga', 'Eurcine 2', '2026-06-03 15:00:00' UNION ALL
  SELECT 'La grazia', 'Eurcine 3', '2026-06-03 15:00:00' UNION ALL
  SELECT 'No other choice', 'Eurcine 4', '2026-06-03 15:00:00' UNION ALL
  SELECT 'Una di famiglia', 'Eurcine 5', '2026-06-03 15:00:00' UNION ALL
  SELECT 'Sentimental value', 'Eurcine 6', '2026-06-03 15:00:00' UNION ALL
  SELECT 'Buen camino', 'Eurcine 7', '2026-06-03 15:00:00' UNION ALL
  SELECT 'La piccola Amelie', 'Eurcine 1', '2026-06-03 20:00:00' UNION ALL
  SELECT 'Vita privata', 'Eurcine 2', '2026-06-03 20:00:00' UNION ALL
  SELECT 'Primavera', 'Eurcine 3', '2026-06-03 20:00:00' UNION ALL
  SELECT 'Norimberga', 'Eurcine 4', '2026-06-03 20:00:00' UNION ALL
  SELECT 'Inland empire', 'Eurcine 5', '2026-06-03 20:00:00' UNION ALL
  SELECT 'Marty supreme', 'Eurcine 6', '2026-06-03 20:00:00' UNION ALL
  SELECT 'La grazia', 'Eurcine 7', '2026-06-03 20:00:00' UNION ALL

  -- Giorno 4 (nuovi film, rimossi 3 titoli)
  SELECT 'Mortal Kombat II', 'Eurcine 1', '2026-06-04 15:00:00' UNION ALL
  SELECT 'Super Mario Galaxy', 'Eurcine 2', '2026-06-04 15:00:00' UNION ALL
  SELECT 'Star Wars - The Mandalorian and Grogu', 'Eurcine 3', '2026-06-04 15:00:00' UNION ALL
  SELECT 'Vita privata', 'Eurcine 4', '2026-06-04 15:00:00' UNION ALL
  SELECT 'Primavera', 'Eurcine 5', '2026-06-04 15:00:00' UNION ALL
  SELECT 'Norimberga', 'Eurcine 6', '2026-06-04 15:00:00' UNION ALL
  SELECT 'La grazia', 'Eurcine 7', '2026-06-04 15:00:00' UNION ALL
  SELECT 'No other choice', 'Eurcine 1', '2026-06-04 20:00:00' UNION ALL
  SELECT 'Una di famiglia', 'Eurcine 2', '2026-06-04 20:00:00' UNION ALL
  SELECT 'Inland empire', 'Eurcine 3', '2026-06-04 20:00:00' UNION ALL
  SELECT 'Marty supreme', 'Eurcine 4', '2026-06-04 20:00:00' UNION ALL
  SELECT 'Mortal Kombat II', 'Eurcine 5', '2026-06-04 20:00:00' UNION ALL
  SELECT 'Super Mario Galaxy', 'Eurcine 6', '2026-06-04 20:00:00' UNION ALL
  SELECT 'Star Wars - The Mandalorian and Grogu', 'Eurcine 7', '2026-06-04 20:00:00' UNION ALL

  -- Giorno 5
  SELECT 'Star Wars - The Mandalorian and Grogu', 'Eurcine 1', '2026-06-05 15:00:00' UNION ALL
  SELECT 'Mortal Kombat II', 'Eurcine 2', '2026-06-05 15:00:00' UNION ALL
  SELECT 'Super Mario Galaxy', 'Eurcine 3', '2026-06-05 15:00:00' UNION ALL
  SELECT 'La grazia', 'Eurcine 4', '2026-06-05 15:00:00' UNION ALL
  SELECT 'No other choice', 'Eurcine 5', '2026-06-05 15:00:00' UNION ALL
  SELECT 'Una di famiglia', 'Eurcine 6', '2026-06-05 15:00:00' UNION ALL
  SELECT 'Vita privata', 'Eurcine 7', '2026-06-05 15:00:00' UNION ALL
  SELECT 'Primavera', 'Eurcine 1', '2026-06-05 20:00:00' UNION ALL
  SELECT 'Norimberga', 'Eurcine 2', '2026-06-05 20:00:00' UNION ALL
  SELECT 'Inland empire', 'Eurcine 3', '2026-06-05 20:00:00' UNION ALL
  SELECT 'Marty supreme', 'Eurcine 4', '2026-06-05 20:00:00' UNION ALL
  SELECT 'Star Wars - The Mandalorian and Grogu', 'Eurcine 5', '2026-06-05 20:00:00' UNION ALL
  SELECT 'Mortal Kombat II', 'Eurcine 6', '2026-06-05 20:00:00' UNION ALL
  SELECT 'Super Mario Galaxy', 'Eurcine 7', '2026-06-05 20:00:00' UNION ALL

  -- Giorno 6
  SELECT 'No other choice', 'Eurcine 1', '2026-06-06 15:00:00' UNION ALL
  SELECT 'Una di famiglia', 'Eurcine 2', '2026-06-06 15:00:00' UNION ALL
  SELECT 'Vita privata', 'Eurcine 3', '2026-06-06 15:00:00' UNION ALL
  SELECT 'Primavera', 'Eurcine 4', '2026-06-06 15:00:00' UNION ALL
  SELECT 'Norimberga', 'Eurcine 5', '2026-06-06 15:00:00' UNION ALL
  SELECT 'Mortal Kombat II', 'Eurcine 6', '2026-06-06 15:00:00' UNION ALL
  SELECT 'Super Mario Galaxy', 'Eurcine 7', '2026-06-06 15:00:00' UNION ALL
  SELECT 'Star Wars - The Mandalorian and Grogu', 'Eurcine 1', '2026-06-06 20:00:00' UNION ALL
  SELECT 'La grazia', 'Eurcine 2', '2026-06-06 20:00:00' UNION ALL
  SELECT 'Inland empire', 'Eurcine 3', '2026-06-06 20:00:00' UNION ALL
  SELECT 'Marty supreme', 'Eurcine 4', '2026-06-06 20:00:00' UNION ALL
  SELECT 'No other choice', 'Eurcine 5', '2026-06-06 20:00:00' UNION ALL
  SELECT 'Una di famiglia', 'Eurcine 6', '2026-06-06 20:00:00' UNION ALL
  SELECT 'Vita privata', 'Eurcine 7', '2026-06-06 20:00:00' UNION ALL

  -- Giorno 7
  SELECT 'Super Mario Galaxy', 'Eurcine 1', '2026-06-07 15:00:00' UNION ALL
  SELECT 'Star Wars - The Mandalorian and Grogu', 'Eurcine 2', '2026-06-07 15:00:00' UNION ALL
  SELECT 'Mortal Kombat II', 'Eurcine 3', '2026-06-07 15:00:00' UNION ALL
  SELECT 'Vita privata', 'Eurcine 4', '2026-06-07 15:00:00' UNION ALL
  SELECT 'Primavera', 'Eurcine 5', '2026-06-07 15:00:00' UNION ALL
  SELECT 'Norimberga', 'Eurcine 6', '2026-06-07 15:00:00' UNION ALL
  SELECT 'La grazia', 'Eurcine 7', '2026-06-07 15:00:00' UNION ALL
  SELECT 'No other choice', 'Eurcine 1', '2026-06-07 20:00:00' UNION ALL
  SELECT 'Una di famiglia', 'Eurcine 2', '2026-06-07 20:00:00' UNION ALL
  SELECT 'Inland empire', 'Eurcine 3', '2026-06-07 20:00:00' UNION ALL
  SELECT 'Marty supreme', 'Eurcine 4', '2026-06-07 20:00:00' UNION ALL
  SELECT 'Super Mario Galaxy', 'Eurcine 5', '2026-06-07 20:00:00' UNION ALL
  SELECT 'Star Wars - The Mandalorian and Grogu', 'Eurcine 6', '2026-06-07 20:00:00' UNION ALL
  SELECT 'Mortal Kombat II', 'Eurcine 7', '2026-06-07 20:00:00'
) sch
JOIN film f ON f.titolo = sch.titolo
JOIN sala s ON s.nome = sch.sala
WHERE NOT EXISTS (
  SELECT 1 FROM programmazione p
  WHERE p.film_id = f.id
    AND p.sala_id = s.id
    AND p.start_at = sch.start_at
);
