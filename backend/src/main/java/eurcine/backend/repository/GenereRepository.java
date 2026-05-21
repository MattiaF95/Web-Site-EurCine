package eurcine.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import eurcine.backend.model.Genere;

public interface GenereRepository extends JpaRepository<Genere, Long> {
}
