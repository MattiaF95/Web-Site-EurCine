package eurcine.backend.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import eurcine.backend.model.AdminSession;

public interface AdminSessionRepository extends JpaRepository<AdminSession, Long> {

    Optional<AdminSession> findByTokenAndRevokedFalse(String token);
}
