package eurcine.backend.repository;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import eurcine.backend.model.Programmazione;
import eurcine.backend.repository.projection.ProgrammazioneGiornalieraView;
import eurcine.backend.repository.projection.ProgrammazioneView;

public interface ProgrammazioneRepository extends JpaRepository<Programmazione, Long> {

    @Query("""
        select p.id
        from Programmazione p
        where p.film.id = :filmId
        """)
    List<Long> findIdsByFilmId(Long filmId);

    List<Programmazione> findAllByFilmId(Long filmId);
    List<Programmazione> findByFilmIdOrderByStartAtAsc(Long filmId);

    @Query("""
        select distinct function('date_format', p.startAt, '%Y-%m-%d')
        from Programmazione p
        order by function('date_format', p.startAt, '%Y-%m-%d') asc
        """)
    List<String> findAvailableDateKeys();

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
        select case when count(p) > 0 then true else false end
        from Programmazione p
        where p.sala.id = :salaId
          and p.startAt < :newEnd
          and function('timestampadd', minute, p.film.durataMin, p.startAt) > :newStart
        """)
    boolean existsSalaOverlap(Long salaId, LocalDateTime newStart, LocalDateTime newEnd);

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
