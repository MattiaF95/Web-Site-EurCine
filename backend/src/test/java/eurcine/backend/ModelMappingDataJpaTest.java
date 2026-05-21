package eurcine.backend;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;
import eurcine.backend.model.Biglietto;
import eurcine.backend.model.Fila;
import eurcine.backend.model.Film;
import eurcine.backend.model.Genere;
import eurcine.backend.model.Lingua;
import eurcine.backend.model.Ordine;
import eurcine.backend.model.Posto;
import eurcine.backend.model.Programmazione;
import eurcine.backend.model.Sala;

@DataJpaTest(properties = {
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
class ModelMappingDataJpaTest {

    @Autowired
    private EntityManager entityManager;

    @Test
    @Transactional
    void shouldPersistCoreRelations() {
        Lingua lingua = new Lingua();
        lingua.setNome("Italiano");
        entityManager.persist(lingua);

        Genere genere = new Genere();
        genere.setNome("Dramma");
        entityManager.persist(genere);

        Film film = new Film();
        film.setTitolo("Vita privata");
        film.setDurataMin(105);
        film.setLingua(lingua);
        film.getGeneri().add(genere);
        entityManager.persist(film);

        Sala sala = new Sala();
        sala.setNome("Eurcine 1");
        entityManager.persist(sala);

        Fila fila = new Fila();
        fila.setSala(sala);
        fila.setLettera("A");
        entityManager.persist(fila);

        Posto posto = new Posto();
        posto.setFila(fila);
        posto.setNumero(1);
        posto.setAttivo(true);
        entityManager.persist(posto);

        Programmazione programmazione = new Programmazione();
        programmazione.setFilm(film);
        programmazione.setSala(sala);
        programmazione.setStartAt(LocalDateTime.of(2026, 5, 20, 16, 0));
        programmazione.setPrezzoBasePre18(new BigDecimal("7.50"));
        programmazione.setPrezzoBasePost18(new BigDecimal("9.50"));
        entityManager.persist(programmazione);

        Ordine ordine = new Ordine();
        ordine.setNumeroOrdine("ORD-001");
        ordine.setNomeCliente("Mario Rossi");
        ordine.setTotale(new BigDecimal("9.50"));
        entityManager.persist(ordine);

        Biglietto biglietto = new Biglietto();
        biglietto.setOrdine(ordine);
        biglietto.setProgrammazione(programmazione);
        biglietto.setPosto(posto);
        biglietto.setPrezzo(new BigDecimal("9.50"));
        entityManager.persist(biglietto);

        entityManager.flush();
        entityManager.clear();

        Biglietto loaded = entityManager.find(Biglietto.class, biglietto.getId());
        assertNotNull(loaded);
        assertNotNull(loaded.getOrdine().getId());
        assertNotNull(loaded.getProgrammazione().getId());
        assertNotNull(loaded.getPosto().getId());
        assertEquals(new BigDecimal("9.50"), loaded.getPrezzo());
    }

    @Test
    @Transactional
    void shouldRejectDuplicateSeatForSameProgrammazione() {
        Lingua lingua = new Lingua();
        lingua.setNome("Italiano");
        entityManager.persist(lingua);

        Film film = new Film();
        film.setTitolo("Primavera");
        film.setDurataMin(120);
        film.setLingua(lingua);
        entityManager.persist(film);

        Sala sala = new Sala();
        sala.setNome("Eurcine 2");
        entityManager.persist(sala);

        Fila fila = new Fila();
        fila.setSala(sala);
        fila.setLettera("A");
        entityManager.persist(fila);

        Posto posto = new Posto();
        posto.setFila(fila);
        posto.setNumero(1);
        posto.setAttivo(true);
        entityManager.persist(posto);

        Programmazione programmazione = new Programmazione();
        programmazione.setFilm(film);
        programmazione.setSala(sala);
        programmazione.setStartAt(LocalDateTime.of(2026, 5, 20, 19, 30));
        programmazione.setPrezzoBasePre18(new BigDecimal("8.00"));
        programmazione.setPrezzoBasePost18(new BigDecimal("10.00"));
        entityManager.persist(programmazione);

        Ordine ordine1 = new Ordine();
        ordine1.setNumeroOrdine("ORD-100");
        ordine1.setNomeCliente("Cliente 1");
        ordine1.setTotale(new BigDecimal("10.00"));
        entityManager.persist(ordine1);

        Biglietto first = new Biglietto();
        first.setOrdine(ordine1);
        first.setProgrammazione(programmazione);
        first.setPosto(posto);
        first.setPrezzo(new BigDecimal("10.00"));
        entityManager.persist(first);
        entityManager.flush();

        Ordine ordine2 = new Ordine();
        ordine2.setNumeroOrdine("ORD-101");
        ordine2.setNomeCliente("Cliente 2");
        ordine2.setTotale(new BigDecimal("10.00"));
        entityManager.persist(ordine2);

        assertThrows(PersistenceException.class, () -> {
            Biglietto duplicate = new Biglietto();
            duplicate.setOrdine(ordine2);
            duplicate.setProgrammazione(programmazione);
            duplicate.setPosto(posto);
            duplicate.setPrezzo(new BigDecimal("10.00"));
            entityManager.persist(duplicate);
            entityManager.flush();
        });
    }
}
