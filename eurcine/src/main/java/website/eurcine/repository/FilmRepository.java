package website.eurcine.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import website.eurcine.model.Film;
import website.eurcine.repository.projection.FilmView;

public interface FilmRepository extends JpaRepository<Film, Long> {

    @Query("""
        select f.titolo as titolo,
               f.durataMin as durataMin,
               l.nome as linguaNome,
               function('group_concat', g.nome) as generiNomi
        from Film f
        join f.lingua l
        left join f.generi g
        group by f.titolo, f.durataMin, l.nome
        order by f.titolo asc
        """)
    List<FilmView> findAllProjected();
}
