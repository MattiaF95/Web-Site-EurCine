package website.eurcine.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import website.eurcine.model.Posto;

public interface PostoRepository extends JpaRepository<Posto, Long> {

    List<Posto> findByFilaIdOrderByNumeroAsc(Long filaId);

    List<Posto> findByFilaIdAndAttivoTrueOrderByNumeroAsc(Long filaId);

    @Query("""
        select count(p.id)
        from Posto p
        join p.fila f
        join f.sala s
        where s.id = :salaId
          and p.attivo = true
        """)
    long countActiveBySalaId(Long salaId);
}
