package eurcine.backend.repository.projection;

import java.time.LocalDateTime;

public interface ProgrammazioneGiornalieraView {

    String getFilmTitolo();

    LocalDateTime getStartAt();

    String getSalaNome();

    Long getPostiDisponibili();
}
