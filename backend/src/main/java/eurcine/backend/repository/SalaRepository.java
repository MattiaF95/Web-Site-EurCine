package eurcine.backend.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import eurcine.backend.model.Sala;
import eurcine.backend.repository.projection.SalaView;

public interface SalaRepository extends JpaRepository<Sala, Long> {

    List<Sala> findAllByOrderByNomeAsc();

    @Query("""
        select s.nome as nome,
               s.descrizione as descrizione,
               function('group_concat', c.caratteristica) as caratteristicheNomi
        from Sala s
        left join s.caratteristiche c
        group by s.nome, s.descrizione
        order by s.nome asc
        """)
    List<SalaView> findAllProjected();
}
