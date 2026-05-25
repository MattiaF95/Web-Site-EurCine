package eurcine.backend.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import eurcine.backend.dto.AuthMeResponse;
import eurcine.backend.dto.LoginRequest;
import eurcine.backend.dto.LoginResponse;
import eurcine.backend.dto.RegisterRequest;
import eurcine.backend.model.Admin;
import eurcine.backend.model.Cliente;
import eurcine.backend.model.Utente;
import eurcine.backend.repository.AdminRepository;
import eurcine.backend.repository.ClienteRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Service
public class AuthService {
    private static final BCryptPasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    private final AdminRepository adminRepository;
    private final ClienteRepository clienteRepository;
    private final JwtService jwtService;

    public AuthService(AdminRepository adminRepository, ClienteRepository clienteRepository, JwtService jwtService) {
        this.adminRepository = adminRepository;
        this.clienteRepository = clienteRepository;
        this.jwtService = jwtService;
    }

    public LoginResult loginWithJwt(LoginRequest request) {
        LoginUser loginUser = validateUserCredentials(request);
        String jwt = jwtService.createToken(loginUser.utente(), loginUser.ruolo());
        LoginResponse response = new LoginResponse(
            loginUser.utente().getId(),
            loginUser.utente().getNome(),
            loginUser.utente().getCognome(),
            loginUser.utente().getEmail(),
            loginUser.ruolo(),
            "Login effettuato",
            jwt
        );
        return new LoginResult(response, jwt);
    }

    public LoginResult registerWithJwt(RegisterRequest request) {
        validateRegisterRequest(request);

        String email = request.email().trim();
        if (adminRepository.findByEmailIgnoreCase(email).isPresent() || clienteRepository.findByEmailIgnoreCase(email).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email già registrata.");
        }

        Cliente cliente = new Cliente();
        cliente.setNome(request.nome().trim());
        cliente.setCognome(request.cognome().trim());
        cliente.setEmail(email);
        cliente.setPasswordHash(PASSWORD_ENCODER.encode(request.password()));
        cliente.setRuolo("USER");
        Cliente saved = clienteRepository.save(cliente);
        String jwt = jwtService.createToken(saved, saved.getRuolo());

        LoginResponse response = new LoginResponse(
            saved.getId(),
            saved.getNome(),
            saved.getCognome(),
            saved.getEmail(),
            saved.getRuolo(),
            "Registrazione completata",
            jwt
        );
        return new LoginResult(response, jwt);
    }

    public AuthMeResponse me(JwtService.UserPrincipal user) {
        return new AuthMeResponse(
            user.getId(),
            user.getNome(),
            user.getCognome(),
            user.getEmail(),
            user.getRuolo(),
            "Sessione valida"
        );
    }

    private LoginUser validateUserCredentials(LoginRequest request) {
        if (request == null || request.email() == null || request.email().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email obbligatoria.");
        }

        if (request.password() == null || request.password().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password obbligatoria.");
        }

        String email = request.email().trim();
        Admin admin = adminRepository.findByEmailIgnoreCase(email).orElse(null);
        Cliente cliente = clienteRepository.findByEmailIgnoreCase(email).orElse(null);
        if (admin == null && cliente == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenziali non valide.");
        }
        Utente utente = admin != null ? admin : cliente;
        String ruolo = admin != null ? admin.getRuolo() : cliente.getRuolo();

        String rawPassword = request.password();
        String storedPassword = utente.getPasswordHash();
        boolean validCredentials = false;

        if (storedPassword != null && storedPassword.startsWith("$2")) {
            validCredentials = PASSWORD_ENCODER.matches(rawPassword, storedPassword);
        } else if (storedPassword != null && storedPassword.equals(rawPassword)) {
            validCredentials = true;
            // Seamless migration from legacy clear-text seed to BCrypt hash.
            utente.setPasswordHash(PASSWORD_ENCODER.encode(rawPassword));
            if (admin != null) {
                adminRepository.save(admin);
            } else {
                clienteRepository.save(cliente);
            }
        }

        if (!validCredentials) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenziali non valide.");
        }
        return new LoginUser(utente, ruolo);
    }

    private void validateRegisterRequest(RegisterRequest request) {
        if (request == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Payload registrazione obbligatorio.");
        }
        if (request.nome() == null || request.nome().trim().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nome obbligatorio.");
        }
        if (request.cognome() == null || request.cognome().trim().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cognome obbligatorio.");
        }
        if (request.email() == null || request.email().trim().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email obbligatoria.");
        }
        if (!request.email().trim().matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email non valida.");
        }
        if (request.password() == null || request.password().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password obbligatoria.");
        }
        if (request.nome().trim().length() > 64 || request.cognome().trim().length() > 64 || request.email().trim().length() > 160) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dati non validi.");
        }
        if (request.password().length() > 255) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password troppo lunga.");
        }
    }

    private record LoginUser(Utente utente, String ruolo) {
    }

    public record LoginResult(LoginResponse response, String jwtToken) {
    }
}
