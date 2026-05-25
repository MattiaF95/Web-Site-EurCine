package eurcine.backend.repository;

import eurcine.backend.model.CaratteristicaSala;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CaratteristicaSalaRepository extends JpaRepository<CaratteristicaSala, Long> {

    List<CaratteristicaSala> findAllByOrderByCategoriaAscCaratteristicaAsc();
}
