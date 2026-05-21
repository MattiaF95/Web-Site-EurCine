package website.eurcine.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrdineRecord(
    Long ordineId,
    String numeroOrdine,
    String nomeCliente,
    BigDecimal totale,
    LocalDateTime createdAt,
    List<BigliettoRecord> biglietti
) {
}
