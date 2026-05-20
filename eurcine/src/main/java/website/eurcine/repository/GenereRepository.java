package website.eurcine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import website.eurcine.model.Genere;

public interface GenereRepository extends JpaRepository<Genere, Long> {
}
