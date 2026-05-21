package eurcine.backend.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import eurcine.backend.dto.SeatDto;
import eurcine.backend.dto.SeatMapDto;
import eurcine.backend.dto.SeatRowDto;
import eurcine.backend.model.Programmazione;
import eurcine.backend.repository.BigliettoRepository;
import eurcine.backend.repository.PostoRepository;
import eurcine.backend.repository.ProgrammazioneRepository;
import eurcine.backend.repository.projection.SeatMapSeatView;

@Service
public class SeatMapService {

    private final ProgrammazioneRepository programmazioneRepository;
    private final PostoRepository postoRepository;
    private final BigliettoRepository bigliettoRepository;

    public SeatMapService(
        ProgrammazioneRepository programmazioneRepository,
        PostoRepository postoRepository,
        BigliettoRepository bigliettoRepository
    ) {
        this.programmazioneRepository = programmazioneRepository;
        this.postoRepository = postoRepository;
        this.bigliettoRepository = bigliettoRepository;
    }

    public SeatMapDto getSeatMap(Long programmazioneId) {
        Programmazione programmazione = programmazioneRepository.findById(programmazioneId)
            .orElseThrow(() -> new IllegalArgumentException("Programmazione non trovata: " + programmazioneId));

        List<SeatMapSeatView> seatViews = postoRepository.findSeatMapBySalaId(programmazione.getSala().getId());
        Set<Long> occupiedSeatIds = bigliettoRepository.findOccupiedPostoIdsByProgrammazioneId(programmazioneId)
            .stream()
            .collect(Collectors.toSet());

        Map<String, List<SeatDto>> rows = new LinkedHashMap<>();
        for (SeatMapSeatView seat : seatViews) {
            String stato = Boolean.FALSE.equals(seat.getAttivo())
                ? "DISABLED"
                : (occupiedSeatIds.contains(seat.getPostoId()) ? "OCCUPIED" : "AVAILABLE");

            rows.computeIfAbsent(seat.getFilaLettera(), ignored -> new ArrayList<>())
                .add(new SeatDto(seat.getPostoId(), seat.getNumero(), stato));
        }

        List<SeatRowDto> righe = rows.entrySet().stream()
            .map(entry -> new SeatRowDto(entry.getKey(), entry.getValue()))
            .toList();

        return new SeatMapDto(
            programmazione.getId(),
            programmazione.getFilm().getTitolo(),
            programmazione.getSala().getNome(),
            programmazione.getStartAt(),
            righe
        );
    }
}
