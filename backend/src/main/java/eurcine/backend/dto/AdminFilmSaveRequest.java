package eurcine.backend.dto;

import java.util.List;

public record AdminFilmSaveRequest(
    String titolo,
    Integer durataMin,
    Long linguaId,
    String trama,
    List<Long> genereIds
) {
}
