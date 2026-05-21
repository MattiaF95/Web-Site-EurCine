package website.eurcine.service;

import java.util.List;
import org.springframework.stereotype.Service;
import website.eurcine.repository.FilmRepository;
import website.eurcine.repository.projection.FilmView;

@Service
public class FilmService {

    private final FilmRepository filmRepository;

    public FilmService(FilmRepository filmRepository) {
        this.filmRepository = filmRepository;
    }

    public List<FilmView> getAll() {
        return filmRepository.findAllProjected();
    }
}
