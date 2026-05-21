package website.eurcine.dto;

import java.time.LocalDateTime;

public record HomeProgrammazioneRecord(
    String filmTitolo,
    LocalDateTime startAt,
    String salaNome,
    Long postiDisponibili
) {
}
