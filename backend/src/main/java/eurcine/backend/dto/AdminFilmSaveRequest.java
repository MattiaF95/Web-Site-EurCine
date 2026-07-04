package eurcine.backend.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import eurcine.backend.validation.NoControlChars;

public record AdminFilmSaveRequest(
    @NotBlank
    @Size(max = 255)
    @NoControlChars
    String titolo,
    @NotNull
    @Min(1)
    Integer durataMin,
    @NotNull
    Long linguaId,
    @NotBlank
    @Size(max = 10000)
    @NoControlChars(allowNewLines = true)
    String trama,
    @NotNull
    @Size(max = 32)
    List<Long> genereIds
) {
}
