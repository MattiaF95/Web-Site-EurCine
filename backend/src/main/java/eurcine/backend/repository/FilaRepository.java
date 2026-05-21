package eurcine.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import eurcine.backend.model.Fila;

public interface FilaRepository extends JpaRepository<Fila, Long> {
}
