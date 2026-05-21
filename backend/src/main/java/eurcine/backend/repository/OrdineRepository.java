package eurcine.backend.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import eurcine.backend.model.Ordine;

public interface OrdineRepository extends JpaRepository<Ordine, Long> {

    Optional<Ordine> findByNumeroOrdine(String numeroOrdine);
}
