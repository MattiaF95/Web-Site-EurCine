package website.eurcine.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import website.eurcine.model.Film;

public interface FilmRepository extends JpaRepository<Film, Long> {

    List<Film> findByTitoloContainingIgnoreCaseOrderByTitoloAsc(String titolo);
}
