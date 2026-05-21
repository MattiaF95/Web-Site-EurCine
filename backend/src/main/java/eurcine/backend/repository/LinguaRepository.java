package eurcine.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import eurcine.backend.model.Lingua;

public interface LinguaRepository extends JpaRepository<Lingua, Long> {
}
