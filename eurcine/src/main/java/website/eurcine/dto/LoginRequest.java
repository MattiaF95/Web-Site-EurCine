package website.eurcine.dto;

public record LoginRequest(
    String email,
    String password
) {
}
