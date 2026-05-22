package eurcine.backend.service;

import eurcine.backend.dto.AdminCatalogOption;
import eurcine.backend.dto.AdminFilmFormData;
import eurcine.backend.dto.AdminFilmMetaResponse;
import eurcine.backend.dto.AdminFilmSaveRequest;
import eurcine.backend.dto.AdminFilmTitleOption;
import eurcine.backend.model.Film;
import eurcine.backend.model.Genere;
import eurcine.backend.model.Lingua;
import eurcine.backend.repository.BigliettoRepository;
import eurcine.backend.repository.FilmRepository;
import eurcine.backend.repository.GenereRepository;
import eurcine.backend.repository.LinguaRepository;
import eurcine.backend.repository.OrdineRepository;
import eurcine.backend.repository.ProgrammazioneRepository;
import jakarta.transaction.Transactional;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AdminFilmManagementService {

    private final FilmRepository filmRepository;
    private final LinguaRepository linguaRepository;
    private final GenereRepository genereRepository;
    private final ProgrammazioneRepository programmazioneRepository;
    private final BigliettoRepository bigliettoRepository;
    private final OrdineRepository ordineRepository;
    private final AuthService authService;

    public AdminFilmManagementService(
        FilmRepository filmRepository,
        LinguaRepository linguaRepository,
        GenereRepository genereRepository,
        ProgrammazioneRepository programmazioneRepository,
        BigliettoRepository bigliettoRepository,
        OrdineRepository ordineRepository,
        AuthService authService
    ) {
        this.filmRepository = filmRepository;
        this.linguaRepository = linguaRepository;
        this.genereRepository = genereRepository;
        this.programmazioneRepository = programmazioneRepository;
        this.bigliettoRepository = bigliettoRepository;
        this.ordineRepository = ordineRepository;
        this.authService = authService;
    }

    public List<AdminFilmTitleOption> getFilmTitles(String token) {
        requireAdmin(token);
        return filmRepository.findAllByOrderByTitoloAsc().stream()
            .map(f -> new AdminFilmTitleOption(f.getId(), f.getTitolo()))
            .toList();
    }

    public AdminFilmMetaResponse getMeta(String token) {
        requireAdmin(token);

        List<AdminCatalogOption> lingue = linguaRepository.findAll().stream()
            .map(l -> new AdminCatalogOption(l.getId(), l.getNome()))
            .toList();

        List<AdminCatalogOption> generi = genereRepository.findAll().stream()
            .map(g -> new AdminCatalogOption(g.getId(), g.getNome()))
            .toList();

        return new AdminFilmMetaResponse(lingue, generi);
    }

    public AdminFilmFormData getFilmById(String token, Long filmId) {
        requireAdmin(token);
        Film film = filmRepository.findById(filmId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Film non trovato."));

        return new AdminFilmFormData(
            film.getId(),
            film.getTitolo(),
            film.getDurataMin(),
            film.getLingua().getId(),
            film.getGeneri().stream().map(Genere::getId).toList()
        );
    }

    @Transactional
    public AdminFilmFormData createFilm(String token, AdminFilmSaveRequest request) {
        requireAdmin(token);
        validateRequest(request, null);

        Film film = new Film();
        applyRequest(film, request);
        Film saved = filmRepository.save(film);
        return getFilmById(token, saved.getId());
    }

    @Transactional
    public AdminFilmFormData updateFilm(String token, Long filmId, AdminFilmSaveRequest request) {
        requireAdmin(token);
        Film film = filmRepository.findById(filmId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Film non trovato."));
        validateRequest(request, filmId);

        applyRequest(film, request);
        Film saved = filmRepository.save(film);
        return getFilmById(token, saved.getId());
    }

    @Transactional
    public void deleteFilm(String token, Long filmId) {
        requireAdmin(token);
        Film film = filmRepository.findById(filmId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Film non trovato."));

        List<Long> programmazioneIds = programmazioneRepository.findIdsByFilmId(filmId);

        if (!programmazioneIds.isEmpty()) {
            bigliettoRepository.deleteByProgrammazioneIdIn(programmazioneIds);
            ordineRepository.deleteOrphanOrders();
            programmazioneRepository.deleteAllByIdInBatch(programmazioneIds);
        }

        filmRepository.delete(film);
        filmRepository.flush();
    }

    private void validateRequest(AdminFilmSaveRequest request, Long currentFilmId) {
        if (request == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Payload obbligatorio.");
        }

        String titolo = request.titolo() == null ? "" : request.titolo().trim();
        if (titolo.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Titolo obbligatorio.");
        }

        if (request.durataMin() == null || request.durataMin() < 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Durata film non valida.");
        }

        if (request.linguaId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Lingua obbligatoria.");
        }

        filmRepository.findByTitoloIgnoreCase(titolo).ifPresent(existing -> {
            if (currentFilmId == null || !existing.getId().equals(currentFilmId)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Esiste già un film con questo titolo.");
            }
        });
    }

    private void applyRequest(Film film, AdminFilmSaveRequest request) {
        Lingua lingua = linguaRepository.findById(request.linguaId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Lingua non trovata."));

        List<Long> genereIds = request.genereIds() == null ? List.of() : request.genereIds();
        Set<Long> dedupGenereIds = new LinkedHashSet<>(genereIds);
        List<Genere> generi = dedupGenereIds.isEmpty()
            ? List.of()
            : genereRepository.findAllById(dedupGenereIds);

        if (generi.size() != dedupGenereIds.size()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Uno o più generi non sono validi.");
        }

        film.setTitolo(request.titolo().trim());
        film.setDurataMin(request.durataMin());
        film.setLingua(lingua);
        film.setGeneri(new LinkedHashSet<>(generi));
    }

    private void requireAdmin(String token) {
        authService.requireAdminFromToken(token);
    }
}
