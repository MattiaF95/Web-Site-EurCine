package website.eurcine.service;

import java.util.List;
import org.springframework.stereotype.Service;
import website.eurcine.repository.SalaRepository;
import website.eurcine.repository.projection.SalaView;

@Service
public class SaleService {

    private final SalaRepository salaRepository;

    public SaleService(SalaRepository salaRepository) {
        this.salaRepository = salaRepository;
    }

    public List<SalaView> getAll() {
        return salaRepository.findAllProjected();
    }
}
