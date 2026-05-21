package website.eurcine.dto;

import java.util.List;

public record CreateOrdineRequest(
    String nomeCliente,
    Long programmazioneId,
    List<Long> postoIds
) {
}
