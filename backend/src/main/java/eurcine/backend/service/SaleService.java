package eurcine.backend.service;

import java.util.List;
import org.springframework.stereotype.Service;
import eurcine.backend.dto.SalaRecord;
import eurcine.backend.repository.SalaRepository;

@Service
public class SaleService {

    private final SalaRepository salaRepository;

    public SaleService(SalaRepository salaRepository) {
        this.salaRepository = salaRepository;
    }

    public List<SalaRecord> getAll() {
        return salaRepository.findAllProjected().stream()
            .map(view -> new SalaRecord(view.getNome()))
            .toList();
    }
}
