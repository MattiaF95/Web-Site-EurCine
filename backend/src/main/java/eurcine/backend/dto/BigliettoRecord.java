package eurcine.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record BigliettoRecord(
    Long bigliettoId,
    Long programmazioneId,
    String filmTitolo,
    String salaNome,
    LocalDateTime startAt,
    String fila,
    Integer postoNumero,
    BigDecimal prezzo
) {
}
