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
INSERT INTO genere (nome)
SELECT 'Azione' WHERE NOT EXISTS (SELECT 1 FROM genere WHERE nome = 'Azione');
INSERT INTO genere (nome)
SELECT 'Fantascienza' WHERE NOT EXISTS (SELECT 1 FROM genere WHERE nome = 'Fantascienza');
INSERT INTO genere (nome)
SELECT 'Avventura' WHERE NOT EXISTS (SELECT 1 FROM genere WHERE nome = 'Avventura');

-- Film
INSERT INTO film (titolo, durata_min, lingua_id, trama)
SELECT 'Vita privata', 105, l.id, 'Quando una donna scompare senza lasciare tracce, il marito e la figlia iniziano a scavare nel passato familiare e scoprono segreti rimasti nascosti per anni, tra identita alterate, bugie affettive e verita che cambiano per sempre i loro legami.'
FROM lingua l
WHERE l.nome = 'Italiano'
  AND NOT EXISTS (SELECT 1 FROM film f WHERE f.titolo = 'Vita privata');

INSERT INTO film (titolo, durata_min, lingua_id, trama)
SELECT 'Primavera', 120, l.id, 'Nella Roma degli anni Settanta, tre giovani musicisti inseguono il sogno di un concerto che puo cambiare la loro vita, mentre la citta attraversa tensioni sociali e politiche che mettono alla prova amicizia, amore e ideali.'
FROM lingua l
WHERE l.nome = 'Italiano'
  AND NOT EXISTS (SELECT 1 FROM film f WHERE f.titolo = 'Primavera');

INSERT INTO film (titolo, durata_min, lingua_id, trama)
SELECT 'Norimberga', 148, l.id, 'Un gruppo di magistrati, avvocati e testimoni affronta uno dei processi piu complessi del dopoguerra, dove il confine tra giustizia e vendetta si assottiglia e ogni testimonianza costringe tutti a riconsiderare responsabilita individuali e collettive.'
FROM lingua l
WHERE l.nome = 'Italiano'
  AND NOT EXISTS (SELECT 1 FROM film f WHERE f.titolo = 'Norimberga');

INSERT INTO film (titolo, durata_min, lingua_id, trama)
SELECT 'Buen camino', 90, l.id, 'Un impiegato in crisi decide di percorrere il Cammino di Santiago senza preparazione e incontra compagni improbabili che, tra gaffe, imprevisti e confessioni notturne, lo aiutano a riscoprire il senso delle piccole scelte quotidiane.'
FROM lingua l
WHERE l.nome = 'Italiano'
  AND NOT EXISTS (SELECT 1 FROM film f WHERE f.titolo = 'Buen camino');

INSERT INTO film (titolo, durata_min, lingua_id, trama)
SELECT 'La grazia', 133, l.id, 'Dopo un errore che ha distrutto una famiglia, una insegnante torna nel suo paese d origine per chiedere perdono, ma trova una comunita divisa tra rabbia e desiderio di ricominciare, in un percorso doloroso verso una possibile riconciliazione.'
FROM lingua l
WHERE l.nome = 'Italiano'
  AND NOT EXISTS (SELECT 1 FROM film f WHERE f.titolo = 'La grazia');

INSERT INTO film (titolo, durata_min, lingua_id, trama)
SELECT 'No other choice', 139, l.id, 'Un contabile insospettabile viene ricattato da una rete criminale dopo aver scoperto movimenti finanziari illeciti: per proteggere la famiglia dovra compiere scelte estreme, entrando in un gioco pericoloso in cui nessuno dice la verita.'
FROM lingua l
WHERE l.nome = 'Inglese'
  AND NOT EXISTS (SELECT 1 FROM film f WHERE f.titolo = 'No other choice');

INSERT INTO film (titolo, durata_min, lingua_id, trama)
SELECT 'Una di famiglia', 131, l.id, 'La morte improvvisa di una zia riunisce parenti che non si parlano da anni: tra eredita contese, vecchi rancori e indizi inattesi, emerge un mistero familiare che obbliga tutti a confrontarsi con cio che hanno taciuto.'
FROM lingua l
WHERE l.nome = 'Italiano'
  AND NOT EXISTS (SELECT 1 FROM film f WHERE f.titolo = 'Una di famiglia');

INSERT INTO film (titolo, durata_min, lingua_id, trama)
SELECT 'La piccola Amelie', 77, l.id, 'In un quartiere sospeso tra fantasia e realta, una bambina timida costruisce mappe segrete per aiutare i vicini a risolvere i loro problemi, scoprendo che il coraggio non e assenza di paura ma scelta di agire nonostante tutto.'
FROM lingua l
WHERE l.nome = 'Francese'
  AND NOT EXISTS (SELECT 1 FROM film f WHERE f.titolo = 'La piccola Amelie');

INSERT INTO film (titolo, durata_min, lingua_id, trama)
SELECT 'Inland empire', 180, l.id, 'Un attrice accetta un ruolo enigmatico e inizia a perdere il confine tra set e vita reale: personaggi che si sdoppiano, ricordi che non le appartengono e corridoi mentali senza uscita trasformano il film in un incubo ipnotico.'
FROM lingua l
WHERE l.nome = 'Inglese'
  AND NOT EXISTS (SELECT 1 FROM film f WHERE f.titolo = 'Inland empire');

INSERT INTO film (titolo, durata_min, lingua_id, trama)
SELECT 'Marty supreme', 150, l.id, 'Marty, talento irregolare del ping pong di periferia, ottiene una chance internazionale ma deve imparare disciplina e fiducia negli altri, mentre la pressione mediatica e i conflitti con il padre minacciano la sua carriera nascente.'
FROM lingua l
WHERE l.nome = 'Inglese'
  AND NOT EXISTS (SELECT 1 FROM film f WHERE f.titolo = 'Marty supreme');

INSERT INTO film (titolo, durata_min, lingua_id, trama)
SELECT 'Sentimental value', 135, l.id, 'Due fratelli tornano nella casa d infanzia per venderla dopo la morte della madre, ma ogni stanza riapre ferite e ricordi: tra oggetti dal valore emotivo e confessioni trattenute per anni, cercano un modo adulto di restare famiglia.'
FROM lingua l
WHERE l.nome = 'Italiano'
  AND NOT EXISTS (SELECT 1 FROM film f WHERE f.titolo = 'Sentimental value');

INSERT INTO film (titolo, durata_min, lingua_id, trama)
SELECT 'Mortal Kombat II', 112, l.id, 'Il nuovo torneo decide il destino dei regni: combattenti umani e guerrieri antichi stringono alleanze instabili per fermare una minaccia cosmica, in una sequenza di sfide letali dove ogni vittoria ha un costo altissimo.'
FROM lingua l
WHERE l.nome = 'Inglese'
  AND NOT EXISTS (SELECT 1 FROM film f WHERE f.titolo = 'Mortal Kombat II');

INSERT INTO film (titolo, durata_min, lingua_id, trama)
SELECT 'Super Mario Galaxy', 104, l.id, 'Mario e Luigi attraversano sistemi planetari imprevedibili per salvare il Regno dei Funghi da una crisi cosmica: tra mondi assurdi, prove cooperative e nuovi alleati, scoprono che il vero potere e restare uniti nelle difficolta.'
FROM lingua l
WHERE l.nome = 'Italiano'
  AND NOT EXISTS (SELECT 1 FROM film f WHERE f.titolo = 'Super Mario Galaxy');

INSERT INTO film (titolo, durata_min, lingua_id, trama)
SELECT 'Star Wars - The Mandalorian and Grogu', 133, l.id, 'Il Mandaloriano e Grogu diventano bersaglio di una nuova caccia interstellare guidata da vecchi nemici imperiali: nel tentativo di proteggere una colonia remota, affronteranno tradimenti, inseguimenti e una scelta che puo cambiare l equilibrio della Forza.'
FROM lingua l
WHERE l.nome = 'Inglese'
  AND NOT EXISTS (SELECT 1 FROM film f WHERE f.titolo = 'Star Wars - The Mandalorian and Grogu');

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

-- Mortal Kombat II
INSERT INTO film_genere (film_id, genere_id)
SELECT f.id, g.id FROM film f, genere g
WHERE f.titolo = 'Mortal Kombat II' AND g.nome IN ('Azione', 'Fantasy')
  AND NOT EXISTS (SELECT 1 FROM film_genere fg WHERE fg.film_id = f.id AND fg.genere_id = g.id);

-- Super Mario Galaxy
INSERT INTO film_genere (film_id, genere_id)
SELECT f.id, g.id FROM film f, genere g
WHERE f.titolo = 'Super Mario Galaxy' AND g.nome IN ('Animazione', 'Avventura', 'Famiglia')
  AND NOT EXISTS (SELECT 1 FROM film_genere fg WHERE fg.film_id = f.id AND fg.genere_id = g.id);

-- Star Wars - The Mandalorian and Grogu
INSERT INTO film_genere (film_id, genere_id)
SELECT f.id, g.id FROM film f, genere g
WHERE f.titolo = 'Star Wars - The Mandalorian and Grogu' AND g.nome IN ('Fantascienza', 'Avventura', 'Azione')
  AND NOT EXISTS (SELECT 1 FROM film_genere fg WHERE fg.film_id = f.id AND fg.genere_id = g.id);
