package website.eurcine.controller;

import java.util.List;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import website.eurcine.dto.SeatMapDto;
import website.eurcine.repository.projection.ProgrammazioneView;
import website.eurcine.service.ProgrammazioneService;
import website.eurcine.service.SeatMapService;

@RestController
@RequestMapping("/api/programmazione")
@CrossOrigin(origins = "http://localhost:4200")
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
    public List<ProgrammazioneView> getAll() {
        return programmazioneService.getAll();
    }

    @GetMapping("/{programmazioneId}/seat-map")
    public SeatMapDto getSeatMap(@PathVariable Long programmazioneId) {
        return seatMapService.getSeatMap(programmazioneId);
    }
}
