package eurcine.backend.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record AdminProgrammazioneBatchCreateRequest(
    @NotNull
    LocalDate giorno,
    @NotNull
    Long filmId,
    @NotNull
    @Size(min = 1, max = 50)
    List<@Valid Item> items
) {
    public record Item(
        @NotNull
        Long salaId,
        @NotNull
        LocalTime orario
    ) {
    }
}
