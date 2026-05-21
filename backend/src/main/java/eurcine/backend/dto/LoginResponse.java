package eurcine.backend.dto;

public record LoginResponse(
    Long adminId,
    String nome,
    String cognome,
    String email,
    String ruolo,
    String message
) {
}
