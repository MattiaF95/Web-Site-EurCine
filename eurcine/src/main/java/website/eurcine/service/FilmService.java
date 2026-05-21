package website.eurcine.service;

import java.util.List;
import org.springframework.stereotype.Service;
import website.eurcine.dto.FilmRecord;
import website.eurcine.repository.FilmRepository;

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
}
