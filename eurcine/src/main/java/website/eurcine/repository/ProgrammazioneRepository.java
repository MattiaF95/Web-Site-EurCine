package website.eurcine.repository;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import website.eurcine.model.Programmazione;
import website.eurcine.repository.projection.ProgrammazioneGiornalieraView;
import website.eurcine.repository.projection.ProgrammazioneView;

public interface ProgrammazioneRepository extends JpaRepository<Programmazione, Long> {

    List<Programmazione> findBySalaIdAndStartAtBetweenOrderByStartAtAsc(
        Long salaId,
        LocalDateTime from,
        LocalDateTime to
    );

    List<Programmazione> findByFilmIdAndStartAtBetweenOrderByStartAtAsc(
        Long filmId,
        LocalDateTime from,
        LocalDateTime to
    );

    @Query("""
        select f.titolo as filmTitolo,
               p.startAt as startAt,
               s.nome as salaNome,
               (count(distinct po.id) - count(distinct b.id)) as postiDisponibili
        from Programmazione p
        join p.film f
        join p.sala s
        join Fila fi on fi.sala = s
        join Posto po on po.fila = fi and po.attivo = true
        left join Biglietto b on b.programmazione = p
        where p.startAt >= :dayStart
          and p.startAt < :dayEnd
        group by p.id, f.titolo, p.startAt, s.nome
        order by p.startAt asc
        """)
    List<ProgrammazioneGiornalieraView> findDailyScheduleWithAvailability(
        LocalDateTime dayStart,
        LocalDateTime dayEnd
    );

    @Query("""
        select p.id as programmazioneId,
               f.titolo as filmTitolo,
               s.nome as salaNome,
               p.startAt as startAt,
               p.prezzoBasePre18 as prezzoBasePre18,
               p.prezzoBasePost18 as prezzoBasePost18
        from Programmazione p
        join p.film f
        join p.sala s
        order by p.startAt asc
        """)
    List<ProgrammazioneView> findAllProjected();
}
