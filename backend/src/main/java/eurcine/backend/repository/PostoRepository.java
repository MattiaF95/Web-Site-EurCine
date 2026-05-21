package eurcine.backend.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import eurcine.backend.model.Posto;
import eurcine.backend.repository.projection.SeatMapSeatView;

public interface PostoRepository extends JpaRepository<Posto, Long> {

    List<Posto> findByFilaIdOrderByNumeroAsc(Long filaId);

    List<Posto> findByFilaIdAndAttivoTrueOrderByNumeroAsc(Long filaId);

    @Query("""
        select p.id as postoId,
               f.lettera as filaLettera,
               p.numero as numero,
               p.attivo as attivo
        from Posto p
        join p.fila f
        join f.sala s
        where s.id = :salaId
        order by f.lettera desc, p.numero asc
        """)
    List<SeatMapSeatView> findSeatMapBySalaId(Long salaId);

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
