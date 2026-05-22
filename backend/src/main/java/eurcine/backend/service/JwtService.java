package eurcine.backend.service;

import eurcine.backend.model.Admin;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class JwtService {
    private final SecretKey key;
    private final long expirationDays;

    public JwtService(
        @Value("${app.jwt.secret}") String secret,
        @Value("${app.jwt.expiration-days:7}") long expirationDays
    ) {
        if (secret == null || secret.length() < 32) {
            throw new IllegalStateException("app.jwt.secret deve avere almeno 32 caratteri.");
        }
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationDays = expirationDays;
    }

    public String createToken(Admin admin) {
        Instant now = Instant.now();
        Instant exp = now.plus(expirationDays, ChronoUnit.DAYS);

        return Jwts.builder()
            .subject(String.valueOf(admin.getId()))
            .issuedAt(Date.from(now))
            .expiration(Date.from(exp))
            .claim("nome", admin.getNome())
            .claim("cognome", admin.getCognome())
            .claim("email", admin.getEmail())
            .claim("ruolo", admin.getRuolo())
            .signWith(key)
            .compact();
    }

    public AdminPrincipal parseToken(String token) {
        if (token == null || token.isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Sessione non presente.");
        }

        try {
            Claims claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
            Long adminId = Long.valueOf(claims.getSubject());
            String nome = claims.get("nome", String.class);
            String cognome = claims.get("cognome", String.class);
            String email = claims.get("email", String.class);
            String ruolo = claims.get("ruolo", String.class);

            if (nome == null || cognome == null || email == null || ruolo == null) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token non valido.");
            }
            return new AdminPrincipal(adminId, nome, cognome, email, ruolo);
        } catch (JwtException | IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token non valido.");
        }
    }

    public static final class AdminPrincipal {
        private final Long id;
        private final String nome;
        private final String cognome;
        private final String email;
        private final String ruolo;

        public AdminPrincipal(Long id, String nome, String cognome, String email, String ruolo) {
            this.id = id;
            this.nome = nome;
            this.cognome = cognome;
            this.email = email;
            this.ruolo = ruolo;
        }

        public Long getId() {
            return id;
        }

        public String getNome() {
            return nome;
        }

        public String getCognome() {
            return cognome;
        }

        public String getEmail() {
            return email;
        }

        public String getRuolo() {
            return ruolo;
        }
    }
}
