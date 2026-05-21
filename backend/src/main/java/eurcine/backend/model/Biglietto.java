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
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(
    name = "biglietto",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_biglietto_programmazione_posto", columnNames = {"programmazione_id", "posto_id"})
    },
    indexes = {
        @Index(name = "idx_biglietto_ordine_id", columnList = "ordine_id"),
        @Index(name = "idx_biglietto_programmazione_id", columnList = "programmazione_id"),
        @Index(name = "idx_biglietto_posto_id", columnList = "posto_id"),
        @Index(name = "idx_biglietto_programmazione_ordine", columnList = "programmazione_id,ordine_id")
    }
)
public class Biglietto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ordine_id", nullable = false)
    private Ordine ordine;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "programmazione_id", nullable = false)
    private Programmazione programmazione;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "posto_id", nullable = false)
    private Posto posto;

    @NotNull
    @DecimalMin(value = "0.00")
    @Column(name = "prezzo", nullable = false, precision = 10, scale = 2)
    private BigDecimal prezzo;
}
