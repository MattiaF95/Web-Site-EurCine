package website.eurcine.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import website.eurcine.repository.ProgrammazioneRepository;
import website.eurcine.repository.projection.ProgrammazioneGiornalieraView;
import website.eurcine.repository.projection.ProgrammazioneView;

@Service
public class ProgrammazioneService {

    private final ProgrammazioneRepository programmazioneRepository;

    public ProgrammazioneService(ProgrammazioneRepository programmazioneRepository) {
        this.programmazioneRepository = programmazioneRepository;
    }

    public List<ProgrammazioneView> getAll() {
        return programmazioneRepository.findAllProjected();
    }

    public List<ProgrammazioneGiornalieraView> getDailySchedule(LocalDate day) {
        LocalDateTime dayStart = day.atStartOfDay();
        LocalDateTime dayEnd = day.plusDays(1).atStartOfDay();
        return programmazioneRepository.findDailyScheduleWithAvailability(dayStart, dayEnd);
    }
}
