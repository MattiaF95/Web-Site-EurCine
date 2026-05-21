package eurcine.backend.dto;

import java.time.LocalDateTime;
import java.util.List;

public record SeatMapDto(
    Long programmazioneId,
    String filmTitolo,
    String salaNome,
    LocalDateTime startAt,
    List<SeatRowDto> righe
) {
}
