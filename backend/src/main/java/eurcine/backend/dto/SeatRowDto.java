package eurcine.backend.dto;

import java.util.List;

public record SeatRowDto(
    String fila,
    List<SeatDto> posti
) {
}
