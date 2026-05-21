package eurcine.backend.controller;

import java.util.List;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import eurcine.backend.dto.FilmRecord;
import eurcine.backend.service.FilmService;

@RestController
@RequestMapping("/api/film")
@CrossOrigin(origins = "http://localhost:4200")
public class FilmController {

    private final FilmService filmService;

    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public List<FilmRecord> getAll() {
        return filmService.getAll();
    }

    @GetMapping("/{titolo}")
    public FilmRecord getOne(@PathVariable String titolo) {
        return filmService.getOne(titolo);
    }
}
