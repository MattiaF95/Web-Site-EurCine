package website.eurcine.service;

import java.util.List;
import org.springframework.stereotype.Service;
import website.eurcine.model.Sala;
import website.eurcine.repository.SalaRepository;

@Service
public class SaleService {

    private final SalaRepository salaRepository;

    public SaleService(SalaRepository salaRepository) {
        this.salaRepository = salaRepository;
    }

    public List<Sala> getAll() {
        return salaRepository.findAll();
    }
}
