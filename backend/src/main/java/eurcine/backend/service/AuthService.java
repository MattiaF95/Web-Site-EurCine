package eurcine.backend.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import eurcine.backend.dto.AuthMeResponse;
import eurcine.backend.dto.LoginRequest;
import eurcine.backend.dto.LoginResponse;
import eurcine.backend.model.Admin;
import eurcine.backend.repository.AdminRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Service
public class AuthService {
    private static final BCryptPasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    private final AdminRepository adminRepository;
    private final JwtService jwtService;

    public AuthService(AdminRepository adminRepository, JwtService jwtService) {
        this.adminRepository = adminRepository;
        this.jwtService = jwtService;
    }

    public LoginResult loginWithJwt(LoginRequest request) {
        Admin admin = validateAdminCredentials(request);
        LoginResponse response = new LoginResponse(
            admin.getId(),
            admin.getNome(),
            admin.getCognome(),
            admin.getEmail(),
            admin.getRuolo(),
            "Login effettuato"
        );
        String jwt = jwtService.createToken(admin);
        return new LoginResult(response, jwt);
    }

    public AuthMeResponse me(JwtService.AdminPrincipal admin) {
        return new AuthMeResponse(
            admin.getId(),
            admin.getNome(),
            admin.getCognome(),
            admin.getEmail(),
            admin.getRuolo(),
            "Sessione valida"
        );
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

        String rawPassword = request.password();
        String storedPassword = admin.getPasswordHash();
        boolean validCredentials = false;

        if (storedPassword != null && storedPassword.startsWith("$2")) {
            validCredentials = PASSWORD_ENCODER.matches(rawPassword, storedPassword);
        } else if (storedPassword != null && storedPassword.equals(rawPassword)) {
            validCredentials = true;
            // Seamless migration from legacy clear-text seed to BCrypt hash.
            admin.setPasswordHash(PASSWORD_ENCODER.encode(rawPassword));
            adminRepository.save(admin);
        }

        if (!validCredentials) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenziali non valide.");
        }
        return admin;
    }

    public record LoginResult(LoginResponse response, String jwtToken) {
    }
}
