package website.eurcine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;
import website.eurcine.model.Biglietto;
import website.eurcine.model.Fila;
import website.eurcine.model.Film;
import website.eurcine.model.Lingua;
import website.eurcine.model.Ordine;
import website.eurcine.model.Posto;
import website.eurcine.model.Programmazione;
import website.eurcine.model.Sala;
import website.eurcine.repository.BigliettoRepository;
import website.eurcine.repository.FilmRepository;
import website.eurcine.repository.OrdineRepository;
import website.eurcine.repository.PostoRepository;
import website.eurcine.repository.ProgrammazioneRepository;
import website.eurcine.repository.projection.ProgrammazioneGiornalieraView;

@DataJpaTest
class RepositoryCustomMethodsDataJpaTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private ProgrammazioneRepository programmazioneRepository;

    @Autowired
    private BigliettoRepository bigliettoRepository;

    @Autowired
    private OrdineRepository ordineRepository;

    @Autowired
    private PostoRepository postoRepository;

    @Autowired
    private FilmRepository filmRepository;

    @Test
    @Transactional
    void shouldReturnDailyScheduleWithAvailabilityAndCustomLookups() {
        Lingua lingua = new Lingua();
        lingua.setNome("Italiano");
        entityManager.persist(lingua);

        Sala sala1 = new Sala();
        sala1.setNome("Eurcine 1");
        entityManager.persist(sala1);

        Sala sala2 = new Sala();
        sala2.setNome("Eurcine 2");
        entityManager.persist(sala2);

        Film film1 = new Film();
        film1.setTitolo("Vita privata");
        film1.setDurataMin(105);
        film1.setLingua(lingua);
        entityManager.persist(film1);

        Film film2 = new Film();
        film2.setTitolo("Primavera");
        film2.setDurataMin(120);
        film2.setLingua(lingua);
        entityManager.persist(film2);

        Fila filaS1 = new Fila();
        filaS1.setSala(sala1);
        filaS1.setLettera("A");
        entityManager.persist(filaS1);

        Fila filaS2 = new Fila();
        filaS2.setSala(sala2);
        filaS2.setLettera("A");
        entityManager.persist(filaS2);

        // Sala1: 4 posti attivi
        Posto s1p1 = createPosto(filaS1, 1, true);
        createPosto(filaS1, 2, true);
        createPosto(filaS1, 3, true);
        createPosto(filaS1, 4, true);

        // Sala2: 2 posti attivi
        createPosto(filaS2, 1, true);
        createPosto(filaS2, 2, true);

        Programmazione p1 = new Programmazione();
        p1.setFilm(film1);
        p1.setSala(sala1);
        p1.setStartAt(LocalDateTime.of(2026, 5, 22, 16, 0));
        p1.setPrezzoBasePre18(new BigDecimal("7.50"));
        p1.setPrezzoBasePost18(new BigDecimal("9.50"));
        entityManager.persist(p1);

        Programmazione p2 = new Programmazione();
        p2.setFilm(film2);
        p2.setSala(sala2);
        p2.setStartAt(LocalDateTime.of(2026, 5, 22, 19, 30));
        p2.setPrezzoBasePre18(new BigDecimal("8.00"));
        p2.setPrezzoBasePost18(new BigDecimal("10.00"));
        entityManager.persist(p2);

        Programmazione p3 = new Programmazione();
        p3.setFilm(film1);
        p3.setSala(sala1);
        p3.setStartAt(LocalDateTime.of(2026, 5, 23, 10, 0));
        p3.setPrezzoBasePre18(new BigDecimal("7.50"));
        p3.setPrezzoBasePost18(new BigDecimal("9.50"));
        entityManager.persist(p3);

        Ordine ordine = new Ordine();
        ordine.setNumeroOrdine("ORD-500");
        ordine.setNomeCliente("Cliente Test");
        ordine.setTotale(new BigDecimal("9.50"));
        entityManager.persist(ordine);

        Biglietto b = new Biglietto();
        b.setOrdine(ordine);
        b.setProgrammazione(p1);
        b.setPosto(s1p1);
        b.setPrezzo(new BigDecimal("9.50"));
        entityManager.persist(b);

        entityManager.flush();

        LocalDate targetDay = LocalDate.of(2026, 5, 22);
        List<ProgrammazioneGiornalieraView> daily = programmazioneRepository.findDailyScheduleWithAvailability(
            targetDay.atStartOfDay(),
            targetDay.plusDays(1).atStartOfDay()
        );

        assertEquals(2, daily.size());
        assertTrue(daily.get(0).getStartAt().isBefore(daily.get(1).getStartAt()));

        ProgrammazioneGiornalieraView first = daily.get(0);
        ProgrammazioneGiornalieraView second = daily.get(1);

        assertEquals("Vita privata", first.getFilmTitolo());
        assertEquals("Eurcine 1", first.getSalaNome());
        assertEquals(3L, first.getPostiDisponibili());

        assertEquals("Primavera", second.getFilmTitolo());
        assertEquals("Eurcine 2", second.getSalaNome());
        assertEquals(2L, second.getPostiDisponibili());

        assertTrue(bigliettoRepository.existsByProgrammazioneIdAndPostoId(p1.getId(), s1p1.getId()));
        assertFalse(bigliettoRepository.existsByProgrammazioneIdAndPostoId(p2.getId(), s1p1.getId()));
        assertEquals(1, bigliettoRepository.countByProgrammazioneId(p1.getId()));
        assertEquals(1, bigliettoRepository.findByOrdineId(ordine.getId()).size());
        assertEquals(1, bigliettoRepository.findByProgrammazioneId(p1.getId()).size());

        assertTrue(ordineRepository.findByNumeroOrdine("ORD-500").isPresent());
        assertEquals(1, filmRepository.findByTitoloContainingIgnoreCaseOrderByTitoloAsc("vita").size());

        assertEquals(4, postoRepository.countActiveBySalaId(sala1.getId()));
        assertEquals(2, postoRepository.countActiveBySalaId(sala2.getId()));
    }

    private Posto createPosto(Fila fila, int numero, boolean attivo) {
        Posto posto = new Posto();
        posto.setFila(fila);
        posto.setNumero(numero);
        posto.setAttivo(attivo);
        entityManager.persist(posto);
        return posto;
    }
}
