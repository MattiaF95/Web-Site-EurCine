package eurcine.backend.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

public record CreateOrdineRequest(
    @NotNull
    Long programmazioneId,
    @NotEmpty
    @Size(max = 64)
    List<Long> postoIds
) {
}
