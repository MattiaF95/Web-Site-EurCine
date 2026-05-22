package eurcine.backend.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import eurcine.backend.dto.HomeProgrammazioneRecord;
import eurcine.backend.dto.ProgrammazioneRecord;
import eurcine.backend.repository.ProgrammazioneRepository;

@Service
public class ProgrammazioneService {

    private final ProgrammazioneRepository programmazioneRepository;

    public ProgrammazioneService(ProgrammazioneRepository programmazioneRepository) {
        this.programmazioneRepository = programmazioneRepository;
    }

    public List<ProgrammazioneRecord> getAll() {
        return programmazioneRepository.findAllProjected().stream()
            .map(view -> new ProgrammazioneRecord(
                view.getProgrammazioneId(),
                view.getFilmTitolo(),
                view.getSalaNome(),
                view.getStartAt(),
                view.getPrezzoBasePre18(),
                view.getPrezzoBasePost18()
            ))
            .toList();
    }

    public List<String> getAvailableDateKeys() {
        return programmazioneRepository.findAvailableDateKeys();
    }

    public List<HomeProgrammazioneRecord> getDailySchedule(LocalDate day) {
        LocalDateTime dayStart = day.atStartOfDay();
        LocalDateTime dayEnd = day.plusDays(1).atStartOfDay();
        return programmazioneRepository.findDailyScheduleWithAvailability(dayStart, dayEnd).stream()
            .map(view -> new HomeProgrammazioneRecord(
                view.getFilmTitolo(),
                view.getStartAt(),
                view.getSalaNome(),
                view.getPostiDisponibili()
            ))
            .toList();
    }
}
