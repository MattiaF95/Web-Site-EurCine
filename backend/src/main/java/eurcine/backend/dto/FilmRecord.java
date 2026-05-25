package eurcine.backend.dto;

public record FilmRecord(
    String titolo,
    Integer durataMin,
    String linguaNome,
    String trama,
    String generiNomi
) {
}
