package eurcine.backend.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
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
        if (nome == null || nome.isBlank() || nome.length() > 64) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nome sala non valido.");
        }

        return salaRepository.findProjectedByNomeIgnoreCase(nome)
            .map(this::toRecord)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sala non trovata."));
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
