## Ragionamento

### Esempio dati utilizzabili

### Film

1. Vita privata
   - Durata: 105’
   - Genere: Dramma, Mistero, Crime
   - Lingua: Italiano
2. Primavera
   - Durata: 120’
   - Genere: Musica, Dramma, Storia
   - Lingua: Italiano
3. Norimberga
   - Durata: 148’
   - Genere: Dramma, Storia
   - Lingua: Italiano
4. Buen camino
   - Durata: 90’
   - Genere: Commedia
   - Lingua: Italiano
5. La grazia
   - Durata: 133’
   - Genere: Commedia, Dramma
   - Lingua: Italiano
6. No other choice
   - Durata: 139’
   - Genere: Commedia, Thriller, Crime
   - Lingua: Inglese
7. Una di famiglia
   - Durata: 131’
   - Genere: Thriller, Mistero
   - Lingua: Italiano
8. La piccola Amelié
   - Durata: 77 min
   - Genere: Animazione, Dramma, Famiglia
   - Lingua: Francese
9. Inland empire
   - Durata: 180’
   - Genere: Horror, Thriller, Fantasy, Dramma, Mistero
   - Lingua: Inglese
10. Marty supreme
    - Durata: 150’
    - Genere: Commedia, Dramma
    - Lingua: Inglese
11. Sentimental value
    - Durata: 135’
    - Genere: Dramma
    - Lingua: Italiano

### Sale

12. Eurcine 1
    - Posti: 176
    - File: 11 (da A a P)
    - Posti per fila: 16
13. Eurcine 2
    - Posti: 184
    - File: 14 (da A a P)
    - Posti per fila: 16
14. Eurcine 3
    - Posti: 120
    - File: 10 (da A ad L)
    - Posti per fila: 12
15. Eurcine 4
    - Posti: 94 + 12 + 11 -> 90
    - File: 6 (da A a F) + 1 (G) + 1 (H) -> 6
    - Posti per fila: 15
16. Eurcine 5
    - Posti: 72
    - File: 8 (da A a G)
    - Posti per fila: 9
17. Eurcine 6
    - Posti: 54 + 7 -> 54
    - File: 6 (da A a F) + 1 (G)
    - Posti per fila: - -> 9
18. Eurcine 7
    - Posti: 80
    - File: 10 (da A a L)
    - Posti per fila: 8

### Programmazione

Esempio un giorno.

19. Vita privata
    - Ore 16:00, Eurcine 7
20. Primavera
    - Ore 16:45, Eurcine 5
    - Ore 19:30, Eurcine 6
21. Norimberga
    - Ore 15:45, Eurcine 4
    - Ore 18:35, Eurcine 7
    - Ore 21:15, Eurcine 7
22. Buen camino
    - Ore 16:00, Eurcine 1
    - Ore 18:00, Eurcine 2
    - Ore 19:45, Eurcine 2
    - Ore 21:30, Eurcine 3
23. La grazia
    - Ore 15:30, Eurcine 2
    - Ore 16:30, Eurcine 3
    - Ore 18:00, Eurcine 1
    - Ore 19:00, Eurcine 3
    - Ore 20:30, Eurcine 1
    - Ore 21:30, Eurcine 2
24. No other choice
    - Ore 18:30, Eurcine 4
    - Ore 21:30, Eurcine 6
25. Una di famiglia
    - Ore 19:00, Eurcine 5
    - Ore 21:30, Eurcine 5
26. La piccola Amelie
    - Ore 15:15, Eurcine 5
27. Inland empire
    - Ore 16:15, Eurcine 6
    - Ore 21:15, Eurcine 4
28. Marty supreme
    - -
29. Sentimental value
    - -

### ENTITY

Entita principali: `Film`, `Genere`, `Lingua`, `Sala`, `Fila`, `Posto`, `Programmazione`, `Ordine`, `Biglietto`.
Entita utenti: `Utente` (astratta), `Admin` (estende `Utente`).

### Film

- `id`
- `titolo`
- `durata_min`
- `lingua_id` (fk -> Lingua)

Note:
- Un film puo avere piu generi, quindi non usare `genere_id` diretto su Film.

### Genere

- `id`
- `nome`

### Film_Genere (tabella ponte)

- `film_id` (fk -> Film)
- `genere_id` (fk -> Genere)

Vincolo:
- unique(`film_id`, `genere_id`)

### Lingua

- `id`
- `nome`

### Sala

- `id`
- `nome`

### Fila

- `id`
- `sala_id` (fk -> Sala)
- `lettera`

Vincolo:
- unique(`sala_id`, `lettera`)

### Posto

- `id`
- `fila_id` (fk -> Fila)
- `numero`
- `attivo` (boolean)

Vincolo:
- unique(`fila_id`, `numero`)

### Programmazione

- `id`
- `film_id` (fk -> Film)
- `sala_id` (fk -> Sala)
- `start_at` (datetime)
- `prezzo_base_pre18`
- `prezzo_base_post18`

Note:
- Meglio `start_at` unico invece di `date` + `orario` separati.
- Prezzi differenziati per fascia oraria: un prezzo per spettacoli prima delle 18:00 e uno per spettacoli dalle 18:00 in poi.

### Ordine

- `id`
- `numero_ordine` (univoco)
- `nome_cliente`
- `totale`
- `created_at`

### Biglietto

- `id`
- `ordine_id` (fk -> Ordine)
- `programmazione_id` (fk -> Programmazione)
- `posto_id` (fk -> Posto)
- `prezzo`

Vincoli:
- unique(`programmazione_id`, `posto_id`) per impedire doppia vendita posto.
- Un ordine puo contenere piu biglietti.

### Utente (astratta)

- `id`
- `nome`
- `cognome`
- `email` (univoca)
- `password_hash`
- `created_at`

Note:
- Classe base astratta per utenti applicativi.
- Mappata con ereditarieta JPA `JOINED` per specializzazioni future.

### Admin

- `id` (fk + pk verso `utente.id` con strategia JOINED)
- `ruolo`

Note:
- Estende `Utente`.
- Tabella separata `admin` con campi specifici amministrativi.

### Altre migliorie logiche

1. Vincoli `unique` composti dove serve (`programmazione_id`, `posto_id`): l'unicita vale sulla coppia, quindi il posto non puo essere venduto due volte nella stessa programmazione, ma puo comparire in programmazioni diverse.
2. Le tabelle devono essere aggiornate a ogni iterazione operativa (acquisto biglietto, inserimento/modifica film, nuove programmazioni, ecc.): dopo la creazione iniziale, il layer `Repository` gestisce insert/update/delete e mantiene lo stato consistente.

### Indici consigliati (FK)

Indici singoli:
- `idx_programmazione_film_id` su `programmazione(film_id)`
- `idx_programmazione_sala_id` su `programmazione(sala_id)`
- `idx_fila_sala_id` su `fila(sala_id)`
- `idx_posto_fila_id` su `posto(fila_id)`
- `idx_biglietto_ordine_id` su `biglietto(ordine_id)`
- `idx_biglietto_programmazione_id` su `biglietto(programmazione_id)`
- `idx_biglietto_posto_id` su `biglietto(posto_id)`
- `idx_film_lingua_id` su `film(lingua_id)`
- `idx_film_genere_film_id` su `film_genere(film_id)`
- `idx_film_genere_genere_id` su `film_genere(genere_id)`

Indici composti utili:
- `idx_programmazione_sala_start_at` su `programmazione(sala_id, start_at)` per ricerca della sala con gli orari spettacoli e verificare sovrapposizioni.
- `idx_programmazione_film_start_at` su `programmazione(film_id, start_at)` per ricerca spettacoli di un film ordinati nel tempo.
- `idx_biglietto_programmazione_ordine` su `biglietto(programmazione_id, ordine_id)` per report e ricerca biglietti per spettacolo/ordine.

Nota:
- Il vincolo `unique(programmazione_id, posto_id)` crea gia un indice composto utile per disponibilita posto nello spettacolo.
