package eurcine.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProgrammazioneRecord(
    Long programmazioneId,
    String filmTitolo,
    String salaNome,
    LocalDateTime startAt,
    BigDecimal prezzoBasePre18,
    BigDecimal prezzoBasePost18
) {
}
