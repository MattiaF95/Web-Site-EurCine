package eurcine.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "admin")
public class Admin extends Utente {

    @NotBlank
    @Column(name = "ruolo", nullable = false, length = 64)
    private String ruolo;
}
