package website.eurcine.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "film", indexes = {
    @Index(name = "idx_film_lingua_id", columnList = "lingua_id")
})
public class Film {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "titolo", nullable = false, length = 255)
    private String titolo;

    @Min(1)
    @Column(name = "durata_min", nullable = false)
    private Integer durataMin;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "lingua_id", nullable = false)
    private Lingua lingua;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "film_genere",
        uniqueConstraints = {
            @UniqueConstraint(name = "uk_film_genere", columnNames = {"film_id", "genere_id"})
        },
        joinColumns = @JoinColumn(name = "film_id", nullable = false),
        inverseJoinColumns = @JoinColumn(name = "genere_id", nullable = false)
    )
    private Set<Genere> generi = new HashSet<>();
}
