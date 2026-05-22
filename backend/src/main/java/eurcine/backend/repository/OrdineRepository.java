package eurcine.backend.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import eurcine.backend.model.Ordine;

public interface OrdineRepository extends JpaRepository<Ordine, Long> {

    Optional<Ordine> findByNumeroOrdine(String numeroOrdine);

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
