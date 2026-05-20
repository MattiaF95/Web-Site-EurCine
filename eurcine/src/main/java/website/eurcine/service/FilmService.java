package website.eurcine.service;

import java.util.List;
import org.springframework.stereotype.Service;
import website.eurcine.model.Film;
import website.eurcine.repository.FilmRepository;

@Service
public class FilmService {

    private final FilmRepository filmRepository;

    public FilmService(FilmRepository filmRepository) {
        this.filmRepository = filmRepository;
    }

    public List<Film> getAll() {
        return filmRepository.findAll();
    }
}
