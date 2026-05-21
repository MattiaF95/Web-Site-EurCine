package eurcine.backend.controller;

import java.time.LocalDate;
import java.util.List;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import eurcine.backend.dto.HomeProgrammazioneRecord;
import eurcine.backend.service.ProgrammazioneService;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class HomeController {

    private final ProgrammazioneService programmazioneService;

    public HomeController(ProgrammazioneService programmazioneService) {
        this.programmazioneService = programmazioneService;
    }

    @GetMapping("/api/home")
    public List<HomeProgrammazioneRecord> home() {
        return programmazioneService.getDailySchedule(LocalDate.now());
    }
}
