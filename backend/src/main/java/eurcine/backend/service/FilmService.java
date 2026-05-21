package eurcine.backend.service;

import java.util.List;
import org.springframework.stereotype.Service;
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
                view.getGeneriNomi()
            ))
            .toList();
    }

    public FilmRecord getOne(String titolo) {
        return filmRepository.findProjectedByTitoloIgnoreCase(titolo)
            .map(view -> new FilmRecord(
                view.getTitolo(),
                view.getDurataMin(),
                view.getLinguaNome(),
                view.getGeneriNomi()
            ))
            .orElseThrow(() -> new IllegalArgumentException("Film non trovato: " + titolo));
    }
}
