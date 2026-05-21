package website.eurcine.dto;

public record AuthMeResponse(
    Long adminId,
    String nome,
    String cognome,
    String email,
    String ruolo,
    String message
) {
}
