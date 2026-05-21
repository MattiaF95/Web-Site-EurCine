package website.eurcine.repository.projection;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface ProgrammazioneView {

    Long getProgrammazioneId();

    String getFilmTitolo();

    String getSalaNome();

    LocalDateTime getStartAt();

    BigDecimal getPrezzoBasePre18();

    BigDecimal getPrezzoBasePost18();
}
