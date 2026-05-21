package website.eurcine.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import website.eurcine.dto.AuthMeResponse;
import website.eurcine.dto.LoginRequest;
import website.eurcine.dto.LoginResponse;
import website.eurcine.model.Admin;
import website.eurcine.model.AdminSession;
import website.eurcine.repository.AdminRepository;
import website.eurcine.repository.AdminSessionRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
public class AuthService {

    private final AdminRepository adminRepository;
    private final AdminSessionRepository adminSessionRepository;

    public AuthService(AdminRepository adminRepository, AdminSessionRepository adminSessionRepository) {
        this.adminRepository = adminRepository;
        this.adminSessionRepository = adminSessionRepository;
    }

    public LoginResponse login(LoginRequest request) {
        Admin admin = validateAdminCredentials(request);
        return new LoginResponse(
            admin.getId(),
            admin.getNome(),
            admin.getCognome(),
            admin.getEmail(),
            admin.getRuolo(),
            "Login effettuato"
        );
    }

    public AuthMeResponse me(String sessionToken) {
        if (sessionToken == null || sessionToken.isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Sessione non presente.");
        }

        AdminSession session = adminSessionRepository.findByTokenAndRevokedFalse(sessionToken)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token non valido."));

        if (session.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Sessione scaduta.");
        }

        Admin admin = session.getAdmin();
        return new AuthMeResponse(
            admin.getId(),
            admin.getNome(),
            admin.getCognome(),
            admin.getEmail(),
            admin.getRuolo(),
            "Sessione valida"
        );
    }

    public String issueSessionToken(LoginRequest request) {
        Admin admin = validateAdminCredentials(request);
        return createSessionToken(admin);
    }

    public LoginResult loginWithSession(LoginRequest request) {
        Admin admin = validateAdminCredentials(request);
        String token = createSessionToken(admin);
        LoginResponse response = new LoginResponse(
            admin.getId(),
            admin.getNome(),
            admin.getCognome(),
            admin.getEmail(),
            admin.getRuolo(),
            "Login effettuato"
        );
        return new LoginResult(response, token);
    }

    private Admin validateAdminCredentials(LoginRequest request) {
        if (request == null || request.email() == null || request.email().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email obbligatoria.");
        }

        if (request.password() == null || request.password().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password obbligatoria.");
        }

        Admin admin = adminRepository.findByEmailIgnoreCase(request.email().trim())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenziali non valide."));

        if (!admin.getPasswordHash().equals(request.password())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenziali non valide.");
        }
        return admin;
    }

    private String createSessionToken(Admin admin) {
        LocalDateTime expiresAt = LocalDateTime.now().plusDays(7).truncatedTo(ChronoUnit.SECONDS);
        String accessToken = UUID.randomUUID().toString().replace("-", "") + UUID.randomUUID().toString().replace("-", "");

        AdminSession session = new AdminSession();
        session.setAdmin(admin);
        session.setToken(accessToken);
        session.setExpiresAt(expiresAt);
        session.setRevoked(false);
        adminSessionRepository.save(session);
        return accessToken;
    }

    public record LoginResult(LoginResponse response, String sessionToken) {
    }
}
