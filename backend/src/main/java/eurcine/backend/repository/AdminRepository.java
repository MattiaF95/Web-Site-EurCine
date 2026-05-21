package eurcine.backend.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import eurcine.backend.model.Admin;

public interface AdminRepository extends JpaRepository<Admin, Long> {

    Optional<Admin> findByEmailIgnoreCase(String email);
}
