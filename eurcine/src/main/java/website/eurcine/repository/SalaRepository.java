package website.eurcine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import website.eurcine.model.Sala;

public interface SalaRepository extends JpaRepository<Sala, Long> {
}
