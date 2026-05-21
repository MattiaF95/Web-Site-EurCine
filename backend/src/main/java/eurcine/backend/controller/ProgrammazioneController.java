package eurcine.backend.controller;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import eurcine.backend.dto.ProgrammazioneRecord;
import eurcine.backend.dto.SeatMapDto;
import eurcine.backend.service.ProgrammazioneService;
import eurcine.backend.service.SeatMapService;

@RestController
@RequestMapping("/api/programmazione")
public class ProgrammazioneController {

    private final ProgrammazioneService programmazioneService;
    private final SeatMapService seatMapService;

    public ProgrammazioneController(
        ProgrammazioneService programmazioneService,
        SeatMapService seatMapService
    ) {
        this.programmazioneService = programmazioneService;
        this.seatMapService = seatMapService;
    }

    @GetMapping
    public List<ProgrammazioneRecord> getAll() {
        return programmazioneService.getAll();
    }

    @GetMapping("/{programmazioneId}/seat-map")
    public SeatMapDto getSeatMap(@PathVariable Long programmazioneId) {
        return seatMapService.getSeatMap(programmazioneId);
    }
}
