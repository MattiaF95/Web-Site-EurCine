package eurcine.backend.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import eurcine.backend.model.Film;
import eurcine.backend.repository.projection.FilmView;

public interface FilmRepository extends JpaRepository<Film, Long> {

    List<Film> findByTitoloContainingIgnoreCaseOrderByTitoloAsc(String titolo);

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

    @Query("""
        select f.titolo as titolo,
               f.durataMin as durataMin,
               l.nome as linguaNome,
               function('group_concat', g.nome) as generiNomi
        from Film f
        join f.lingua l
        left join f.generi g
        where lower(f.titolo) = lower(:titolo)
        group by f.titolo, f.durataMin, l.nome
        """)
    Optional<FilmView> findProjectedByTitoloIgnoreCase(String titolo);
}
