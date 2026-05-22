package eurcine.backend.dto;

import java.util.List;

public record AdminProgrammazioneCatalogResponse(
    List<AdminFilmTitleOption> film,
    List<AdminCatalogOption> sale
) {
}
