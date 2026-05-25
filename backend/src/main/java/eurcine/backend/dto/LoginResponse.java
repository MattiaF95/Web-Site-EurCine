package eurcine.backend.dto;

public record LoginResponse(
    Long utenteId,
    String nome,
    String cognome,
    String email,
    String ruolo,
    String message,
    String token
) {
}
