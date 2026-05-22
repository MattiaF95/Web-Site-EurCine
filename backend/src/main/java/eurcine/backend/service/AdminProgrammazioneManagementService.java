package eurcine.backend.service;

import eurcine.backend.dto.AdminCatalogOption;
import eurcine.backend.dto.AdminFilmTitleOption;
import eurcine.backend.dto.AdminProgrammazioneBatchCreateRequest;
import eurcine.backend.dto.AdminProgrammazioneBatchCreateResponse;
import eurcine.backend.dto.AdminProgrammazioneCatalogResponse;
import eurcine.backend.dto.AdminProgrammazioneCreatedItem;
import eurcine.backend.model.Film;
import eurcine.backend.model.Programmazione;
import eurcine.backend.model.Sala;
import eurcine.backend.repository.BigliettoRepository;
import eurcine.backend.repository.FilmRepository;
import eurcine.backend.repository.OrdineRepository;
import eurcine.backend.repository.ProgrammazioneRepository;
import eurcine.backend.repository.SalaRepository;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AdminProgrammazioneManagementService {

    private static final BigDecimal PREZZO_PRE18 = new BigDecimal("4.90");
    private static final BigDecimal PREZZO_POST18 = new BigDecimal("7.90");

    private final AuthService authService;
    private final FilmRepository filmRepository;
    private final SalaRepository salaRepository;
    private final ProgrammazioneRepository programmazioneRepository;
    private final BigliettoRepository bigliettoRepository;
    private final OrdineRepository ordineRepository;

    public AdminProgrammazioneManagementService(
        AuthService authService,
        FilmRepository filmRepository,
        SalaRepository salaRepository,
        ProgrammazioneRepository programmazioneRepository,
        BigliettoRepository bigliettoRepository,
        OrdineRepository ordineRepository
    ) {
        this.authService = authService;
        this.filmRepository = filmRepository;
        this.salaRepository = salaRepository;
        this.programmazioneRepository = programmazioneRepository;
        this.bigliettoRepository = bigliettoRepository;
        this.ordineRepository = ordineRepository;
    }

    public AdminProgrammazioneCatalogResponse getCatalog(String token) {
        requireAdmin(token);

        List<AdminFilmTitleOption> film = filmRepository.findAllByOrderByTitoloAsc().stream()
            .map(f -> new AdminFilmTitleOption(f.getId(), f.getTitolo()))
            .toList();

        List<AdminCatalogOption> sale = salaRepository.findAllByOrderByNomeAsc().stream()
            .map(s -> new AdminCatalogOption(s.getId(), s.getNome()))
            .toList();

        return new AdminProgrammazioneCatalogResponse(film, sale);
    }

    @Transactional
    public AdminProgrammazioneBatchCreateResponse createProgrammazioni(
        String token,
        AdminProgrammazioneBatchCreateRequest request
    ) {
        requireAdmin(token);
        validate(request);

        if (!filmRepository.existsById(request.filmId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Film non trovato.");
        }
        Film film = filmRepository.getReferenceById(request.filmId());

        List<AdminProgrammazioneCreatedItem> created = new ArrayList<>();

        for (int i = 0; i < request.items().size(); i++) {
            AdminProgrammazioneBatchCreateRequest.Item item = request.items().get(i);

            if (!salaRepository.existsById(item.salaId())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sala non trovata.");
            }
            Sala sala = salaRepository.getReferenceById(item.salaId());

            LocalDateTime startAt = LocalDateTime.of(request.giorno(), item.orario());
            LocalDateTime endAt = startAt.plusMinutes(film.getDurataMin());

            boolean overlap = programmazioneRepository.existsSalaOverlap(sala.getId(), startAt, endAt);
            if (overlap) {
                throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Conflitto programmazione alla riga " + (i + 1) + ": sala già occupata in questo intervallo."
                );
            }

            Programmazione p = new Programmazione();
            p.setFilm(film);
            p.setSala(sala);
            p.setStartAt(startAt);
            p.setPrezzoBasePre18(PREZZO_PRE18);
            p.setPrezzoBasePost18(PREZZO_POST18);

            Programmazione saved = programmazioneRepository.save(p);

            created.add(new AdminProgrammazioneCreatedItem(
                saved.getId(),
                film.getId(),
                film.getTitolo(),
                sala.getId(),
                sala.getNome(),
                saved.getStartAt(),
                saved.getPrezzoBasePre18(),
                saved.getPrezzoBasePost18()
            ));
        }

        return new AdminProgrammazioneBatchCreateResponse(
            "Programmazione inserita con successo.",
            created.size(),
            created
        );
    }

    public List<AdminProgrammazioneCreatedItem> getProgrammazioniByFilmId(String token, Long filmId) {
        requireAdmin(token);
        if (filmId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Film obbligatorio.");
        }

        return programmazioneRepository.findByFilmIdOrderByStartAtAsc(filmId).stream()
            .map(p -> new AdminProgrammazioneCreatedItem(
                p.getId(),
                p.getFilm().getId(),
                p.getFilm().getTitolo(),
                p.getSala().getId(),
                p.getSala().getNome(),
                p.getStartAt(),
                p.getPrezzoBasePre18(),
                p.getPrezzoBasePost18()
            ))
            .toList();
    }

    @Transactional
    public void deleteProgrammazione(String token, Long programmazioneId) {
        requireAdmin(token);
        Programmazione programmazione = programmazioneRepository.findById(programmazioneId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Programmazione non trovata."));

        bigliettoRepository.deleteByProgrammazioneId(programmazioneId);
        ordineRepository.deleteOrphanOrders();
        programmazioneRepository.delete(programmazione);
        programmazioneRepository.flush();
    }

    private void validate(AdminProgrammazioneBatchCreateRequest request) {
        if (request == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Payload obbligatorio.");
        }
        if (request.giorno() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Giorno obbligatorio.");
        }
        if (request.filmId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Film obbligatorio.");
        }
        if (request.items() == null || request.items().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Inserire almeno una riga sala/orario.");
        }

        for (int i = 0; i < request.items().size(); i++) {
            AdminProgrammazioneBatchCreateRequest.Item item = request.items().get(i);
            if (item == null || item.salaId() == null || item.orario() == null) {
                throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Riga " + (i + 1) + " incompleta: sala e orario sono obbligatori."
                );
            }

            if (item.orario().getMinute() % 10 != 0) {
                throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Riga " + (i + 1) + ": l'orario deve avere intervalli di 10 minuti."
                );
            }
        }
    }

    private void requireAdmin(String token) {
        authService.requireAdminFromToken(token);
    }
}
