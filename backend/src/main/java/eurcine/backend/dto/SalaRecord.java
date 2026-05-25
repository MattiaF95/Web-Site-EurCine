package eurcine.backend.dto;

public record SalaRecord(
    String nome,
    String descrizione,
    String caratteristicheNomi,
    Long postiTotali,
    Long fileTotali,
    String sequenzaFile
) {
}
