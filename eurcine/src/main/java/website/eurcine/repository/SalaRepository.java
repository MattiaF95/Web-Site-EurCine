package website.eurcine.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import website.eurcine.model.Sala;
import website.eurcine.repository.projection.SalaView;

public interface SalaRepository extends JpaRepository<Sala, Long> {

    @Query("""
        select s.nome as nome
        from Sala s
        order by s.nome asc
        """)
    List<SalaView> findAllProjected();
}
