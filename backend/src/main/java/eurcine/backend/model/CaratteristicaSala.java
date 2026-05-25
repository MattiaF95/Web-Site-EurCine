package eurcine.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "caratteristica_sala")
public class CaratteristicaSala {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "categoria", nullable = false, length = 64)
    private String categoria;

    @NotBlank
    @Column(name = "caratteristica", nullable = false, length = 128)
    private String caratteristica;
}
