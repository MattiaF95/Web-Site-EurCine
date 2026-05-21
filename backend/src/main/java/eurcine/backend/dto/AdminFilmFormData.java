package eurcine.backend.dto;

import java.util.List;

public record AdminFilmFormData(
    Long id,
    String titolo,
    Integer durataMin,
    Long linguaId,
    List<Long> genereIds
) {
}
