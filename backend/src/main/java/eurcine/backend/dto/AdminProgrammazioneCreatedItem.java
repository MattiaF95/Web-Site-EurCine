package eurcine.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AdminProgrammazioneCreatedItem(
    Long programmazioneId,
    Long filmId,
    String filmTitolo,
    Long salaId,
    String salaNome,
    LocalDateTime startAt,
    BigDecimal prezzoBasePre18,
    BigDecimal prezzoBasePost18
) {
}
