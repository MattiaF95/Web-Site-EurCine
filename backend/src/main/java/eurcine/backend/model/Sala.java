package eurcine.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "sala")
public class Sala {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "nome", nullable = false, length = 64)
    private String nome;

    @Column(name = "descrizione", length = 255)
    private String descrizione;

    @ManyToMany
    @JoinTable(
        name = "sala_caratteristica",
        uniqueConstraints = {
            @UniqueConstraint(name = "uk_sala_caratteristica", columnNames = {"sala_id", "caratteristica_sala_id"})
        },
        joinColumns = @JoinColumn(name = "sala_id", nullable = false),
        inverseJoinColumns = @JoinColumn(name = "caratteristica_sala_id", nullable = false)
    )
    private Set<CaratteristicaSala> caratteristiche = new HashSet<>();
}
