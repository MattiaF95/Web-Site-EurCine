package eurcine.backend.dto;

import java.util.List;

public record AdminFilmMetaResponse(
    List<AdminCatalogOption> lingue,
    List<AdminCatalogOption> generi
) {
}
