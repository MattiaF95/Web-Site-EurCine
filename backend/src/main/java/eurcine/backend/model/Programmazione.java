package eurcine.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "programmazione", indexes = {
    @Index(name = "idx_programmazione_film_id", columnList = "film_id"),
    @Index(name = "idx_programmazione_sala_id", columnList = "sala_id"),
    @Index(name = "idx_programmazione_sala_start_at", columnList = "sala_id,start_at"),
    @Index(name = "idx_programmazione_film_start_at", columnList = "film_id,start_at")
})
public class Programmazione {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "film_id", nullable = false)
    private Film film;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sala_id", nullable = false)
    private Sala sala;

    @NotNull
    @Column(name = "start_at", nullable = false)
    private LocalDateTime startAt;

    @NotNull
    @DecimalMin(value = "0.00")
    @Column(name = "prezzo_base_pre18", nullable = false, precision = 10, scale = 2)
    private BigDecimal prezzoBasePre18;

    @NotNull
    @DecimalMin(value = "0.00")
    @Column(name = "prezzo_base_post18", nullable = false, precision = 10, scale = 2)
    private BigDecimal prezzoBasePost18;
}
