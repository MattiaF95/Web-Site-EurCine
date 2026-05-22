package eurcine.backend.dto;

import java.util.List;

public record AdminProgrammazioneBatchCreateResponse(
    String message,
    int createdCount,
    List<AdminProgrammazioneCreatedItem> created
) {
}
