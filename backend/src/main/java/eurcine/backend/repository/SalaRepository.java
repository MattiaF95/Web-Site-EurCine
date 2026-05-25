package eurcine.backend.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import eurcine.backend.model.Sala;
import eurcine.backend.repository.projection.SalaView;

public interface SalaRepository extends JpaRepository<Sala, Long> {

    List<Sala> findAllByOrderByNomeAsc();

    @Query(value = """
        SELECT
          s.nome AS nome,
          s.descrizione AS descrizione,
          GROUP_CONCAT(DISTINCT c.caratteristica ORDER BY c.caratteristica SEPARATOR ', ') AS caratteristicheNomi,
          COUNT(DISTINCT p.id) AS postiTotali,
          COUNT(DISTINCT f.id) AS fileTotali,
          CASE
            WHEN COUNT(DISTINCT f.id) = 0 THEN NULL
            ELSE CONCAT(MIN(f.lettera), '-', MAX(f.lettera))
          END AS sequenzaFile
        FROM sala s
        LEFT JOIN fila f ON f.sala_id = s.id
        LEFT JOIN posto p ON p.fila_id = f.id
        LEFT JOIN sala_caratteristica sc ON sc.sala_id = s.id
        LEFT JOIN caratteristica_sala c ON c.id = sc.caratteristica_sala_id
        GROUP BY s.id, s.nome, s.descrizione
        ORDER BY s.nome ASC
        """, nativeQuery = true)
    List<SalaView> findAllProjected();

    @Query(value = """
        SELECT
          s.nome AS nome,
          s.descrizione AS descrizione,
          GROUP_CONCAT(DISTINCT c.caratteristica ORDER BY c.caratteristica SEPARATOR ', ') AS caratteristicheNomi,
          COUNT(DISTINCT p.id) AS postiTotali,
          COUNT(DISTINCT f.id) AS fileTotali,
          CASE
            WHEN COUNT(DISTINCT f.id) = 0 THEN NULL
            ELSE CONCAT(MIN(f.lettera), '-', MAX(f.lettera))
          END AS sequenzaFile
        FROM sala s
        LEFT JOIN fila f ON f.sala_id = s.id
        LEFT JOIN posto p ON p.fila_id = f.id
        LEFT JOIN sala_caratteristica sc ON sc.sala_id = s.id
        LEFT JOIN caratteristica_sala c ON c.id = sc.caratteristica_sala_id
        WHERE LOWER(s.nome) = LOWER(:nome)
        GROUP BY s.id, s.nome, s.descrizione
        """, nativeQuery = true)
    Optional<SalaView> findProjectedByNomeIgnoreCase(@Param("nome") String nome);
}
