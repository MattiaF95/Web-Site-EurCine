package eurcine.backend.repository;

import java.util.Optional;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import eurcine.backend.model.Ordine;

public interface OrdineRepository extends JpaRepository<Ordine, Long> {

    Optional<Ordine> findByNumeroOrdine(String numeroOrdine);
    Optional<Ordine> findByIdAndUtenteId(Long id, Long utenteId);
    Optional<Ordine> findByNumeroOrdineAndUtenteId(String numeroOrdine, Long utenteId);
    List<Ordine> findAllByUtenteIdOrderByCreatedAtDesc(Long utenteId);
    List<Ordine> findAllByOrderByCreatedAtDesc();

    @Modifying
    @Query("""
        delete from Ordine o
        where not exists (
            select 1
            from Biglietto b
            where b.ordine = o
        )
        """)
    int deleteOrphanOrders();
}
