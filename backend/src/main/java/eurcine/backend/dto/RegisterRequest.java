package eurcine.backend.dto;

public record RegisterRequest(
    String nome,
    String cognome,
    String email,
    String password
) {
}
