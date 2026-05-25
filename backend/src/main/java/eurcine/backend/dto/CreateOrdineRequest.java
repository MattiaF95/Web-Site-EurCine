package eurcine.backend.dto;

import java.util.List;

public record CreateOrdineRequest(
    Long programmazioneId,
    List<Long> postoIds
) {
}
