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
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(
    name = "posto",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_posto_fila_numero", columnNames = {"fila_id", "numero"})
    },
    indexes = {
        @Index(name = "idx_posto_fila_id", columnList = "fila_id")
    }
)
public class Posto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fila_id", nullable = false)
    private Fila fila;

    @NotNull
    @Min(1)
    @Column(name = "numero", nullable = false)
    private Integer numero;

    @NotNull
    @Column(name = "attivo", nullable = false)
    private Boolean attivo = Boolean.TRUE;
}
