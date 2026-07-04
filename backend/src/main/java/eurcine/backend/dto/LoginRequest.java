package eurcine.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import eurcine.backend.validation.NoControlChars;

public record LoginRequest(
    @NotBlank
    @Email
    @Size(max = 160)
    @NoControlChars
    String email,
    @NotBlank
    @Size(min = 6, max = 255)
    @NoControlChars
    String password
) {
}
