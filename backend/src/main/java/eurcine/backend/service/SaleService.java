package eurcine.backend.service;

import java.util.List;
import org.springframework.stereotype.Service;
import eurcine.backend.dto.SalaRecord;
import eurcine.backend.repository.projection.SalaView;
import eurcine.backend.repository.SalaRepository;

@Service
public class SaleService {

    private final SalaRepository salaRepository;

    public SaleService(SalaRepository salaRepository) {
        this.salaRepository = salaRepository;
    }

    public List<SalaRecord> getAll() {
        return salaRepository.findAllProjected().stream()
            .map(this::toRecord)
            .toList();
    }

    public SalaRecord getOne(String nome) {
        return salaRepository.findProjectedByNomeIgnoreCase(nome)
            .map(this::toRecord)
            .orElseThrow(() -> new IllegalArgumentException("Sala non trovata: " + nome));
    }

    private SalaRecord toRecord(SalaView view) {
        return new SalaRecord(
            view.getNome(),
            view.getDescrizione(),
            view.getCaratteristicheNomi(),
            view.getPostiTotali(),
            view.getFileTotali(),
            view.getSequenzaFile()
        );
    }
}
