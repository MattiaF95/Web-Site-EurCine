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

    public AdminFilmManagementService(
        FilmRepository filmRepository,
        LinguaRepository linguaRepository,
        GenereRepository genereRepository,
        ProgrammazioneRepository programmazioneRepository,
        BigliettoRepository bigliettoRepository,
        OrdineRepository ordineRepository
    ) {
        this.filmRepository = filmRepository;
        this.linguaRepository = linguaRepository;
        this.genereRepository = genereRepository;
        this.programmazioneRepository = programmazioneRepository;
        this.bigliettoRepository = bigliettoRepository;
        this.ordineRepository = ordineRepository;
    }

    public List<AdminFilmTitleOption> getFilmTitles() {
        return filmRepository.findAllByOrderByTitoloAsc().stream()
            .map(f -> new AdminFilmTitleOption(f.getId(), f.getTitolo()))
            .toList();
    }

    public AdminFilmMetaResponse getMeta() {
        List<AdminCatalogOption> lingue = linguaRepository.findAll().stream()
            .map(l -> new AdminCatalogOption(l.getId(), l.getNome()))
            .toList();

        List<AdminCatalogOption> generi = genereRepository.findAll().stream()
            .map(g -> new AdminCatalogOption(g.getId(), g.getNome()))
            .toList();

        return new AdminFilmMetaResponse(lingue, generi);
    }

    public AdminFilmFormData getFilmById(Long filmId) {
        Film film = filmRepository.findById(filmId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Film non trovato."));

        return new AdminFilmFormData(
            film.getId(),
            film.getTitolo(),
            film.getDurataMin(),
            film.getLingua().getId(),
            film.getTrama(),
            film.getGeneri().stream().map(Genere::getId).toList()
        );
    }

    @Transactional
    public AdminFilmFormData createFilm(AdminFilmSaveRequest request) {
        validateRequest(request, null);

        Film film = new Film();
        applyRequest(film, request);
        Film saved = filmRepository.save(film);
        return getFilmById(saved.getId());
    }

    @Transactional
    public AdminFilmFormData updateFilm(Long filmId, AdminFilmSaveRequest request) {
        Film film = filmRepository.findById(filmId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Film non trovato."));
        validateRequest(request, filmId);

        applyRequest(film, request);
        Film saved = filmRepository.save(film);
        return getFilmById(saved.getId());
    }

    @Transactional
    public void deleteFilm(Long filmId) {
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

        String trama = request.trama() == null ? "" : request.trama().trim();
        if (trama.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Trama obbligatoria.");
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
        film.setTrama(request.trama().trim());
        film.setGeneri(new LinkedHashSet<>(generi));
    }
}
