-- Seed programmazione giornaliera (idempotente)
-- Data di riferimento: 2026-06-01
-- Prezzi base: 4.90 (<18:00) e 7.90 (>=18:00)

INSERT INTO programmazione (film_id, sala_id, start_at, prezzo_base_pre18, prezzo_base_post18)
SELECT f.id, s.id, sch.start_at, 4.90, 7.90
FROM (
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
  SELECT 'Norimberga', 'Eurcine 7', '2026-06-01 20:00:00'
) sch
JOIN film f ON f.titolo = sch.titolo
JOIN sala s ON s.nome = sch.sala
WHERE NOT EXISTS (
  SELECT 1 FROM programmazione p
  WHERE p.film_id = f.id
    AND p.sala_id = s.id
    AND p.start_at = sch.start_at
);
