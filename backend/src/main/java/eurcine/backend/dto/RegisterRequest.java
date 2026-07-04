package eurcine.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import eurcine.backend.validation.NoControlChars;

public record RegisterRequest(
    @NotBlank
    @Size(max = 64)
    @Pattern(regexp = "^[\\p{L}\\p{M}\\s'.-]+$", message = "Nome non valido.")
    @NoControlChars
    String nome,
    @NotBlank
    @Size(max = 64)
    @Pattern(regexp = "^[\\p{L}\\p{M}\\s'.-]+$", message = "Cognome non valido.")
    @NoControlChars
    String cognome,
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
