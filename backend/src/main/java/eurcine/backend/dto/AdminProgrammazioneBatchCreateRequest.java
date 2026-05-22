package eurcine.backend.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record AdminProgrammazioneBatchCreateRequest(
    LocalDate giorno,
    Long filmId,
    List<Item> items
) {
    public record Item(
        Long salaId,
        LocalTime orario
    ) {
    }
}
