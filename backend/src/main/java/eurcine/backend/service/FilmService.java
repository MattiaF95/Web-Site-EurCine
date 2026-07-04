package eurcine.backend.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import eurcine.backend.dto.FilmRecord;
import eurcine.backend.repository.FilmRepository;

@Service
public class FilmService {

    private final FilmRepository filmRepository;

    public FilmService(FilmRepository filmRepository) {
        this.filmRepository = filmRepository;
    }

    public List<FilmRecord> getAll() {
        return filmRepository.findAllProjected().stream()
            .map(view -> new FilmRecord(
                view.getTitolo(),
                view.getDurataMin(),
                view.getLinguaNome(),
                view.getTrama(),
                view.getGeneriNomi()
            ))
            .toList();
    }

    public FilmRecord getOne(String titolo) {
        if (titolo == null || titolo.isBlank() || titolo.length() > 255) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Titolo film non valido.");
        }

        return filmRepository.findProjectedByTitoloIgnoreCase(titolo)
            .map(view -> new FilmRecord(
                view.getTitolo(),
                view.getDurataMin(),
                view.getLinguaNome(),
                view.getTrama(),
                view.getGeneriNomi()
            ))
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Film non trovato."));
    }
}
