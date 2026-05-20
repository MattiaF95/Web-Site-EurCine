package website.eurcine.repository.projection;

import java.time.LocalDateTime;

public interface ProgrammazioneGiornalieraView {

    Long getProgrammazioneId();

    String getFilmTitolo();

    LocalDateTime getStartAt();

    String getSalaNome();

    Long getPostiDisponibili();
}
