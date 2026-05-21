package website.eurcine.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import website.eurcine.model.Biglietto;

public interface BigliettoRepository extends JpaRepository<Biglietto, Long> {

    boolean existsByProgrammazioneIdAndPostoId(Long programmazioneId, Long postoId);

    long countByProgrammazioneId(Long programmazioneId);

    List<Biglietto> findByOrdineId(Long ordineId);

    List<Biglietto> findByProgrammazioneId(Long programmazioneId);

    @Query("""
        select b.posto.id
        from Biglietto b
        where b.programmazione.id = :programmazioneId
        """)
    List<Long> findOccupiedPostoIdsByProgrammazioneId(Long programmazioneId);
}
