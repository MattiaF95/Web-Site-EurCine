package website.eurcine.controller;

import java.util.List;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import website.eurcine.model.Programmazione;
import website.eurcine.service.ProgrammazioneService;

@RestController
@RequestMapping("/api/programmazione")
@CrossOrigin(origins = "http://localhost:4200")
public class ProgrammazioneController {

    private final ProgrammazioneService programmazioneService;

    public ProgrammazioneController(ProgrammazioneService programmazioneService) {
        this.programmazioneService = programmazioneService;
    }

    @GetMapping
    public List<Programmazione> getAll() {
        return programmazioneService.getAll();
    }
}
