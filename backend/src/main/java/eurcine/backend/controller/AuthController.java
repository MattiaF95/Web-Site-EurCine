package eurcine.backend.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import eurcine.backend.dto.AuthMeResponse;
import eurcine.backend.dto.LoginRequest;
import eurcine.backend.dto.LoginResponse;
import eurcine.backend.service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final String SESSION_COOKIE_NAME = "eurcine_session";
    private final AuthService authService;
    private final Environment environment;

    public AuthController(AuthService authService, Environment environment) {
        this.authService = authService;
        this.environment = environment;
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request, HttpServletResponse response) {
        AuthService.LoginResult loginResult = authService.loginWithJwt(request);
        boolean production = isProduction();

        ResponseCookie cookie = ResponseCookie.from(SESSION_COOKIE_NAME, loginResult.jwtToken())
            .httpOnly(true)
            .secure(production)
            .path("/")
            .maxAge(7 * 24 * 60 * 60)
            .sameSite(production ? "None" : "Lax")
            .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return loginResult.response();
    }

    @GetMapping("/me")
    public AuthMeResponse me(@CookieValue(name = SESSION_COOKIE_NAME, required = false) String token) {
        return authService.me(authService.requireAdminFromToken(token));
    }

    @PostMapping("/logout")
    public void logout(HttpServletResponse response) {
        boolean production = isProduction();
        ResponseCookie cookie = ResponseCookie.from(SESSION_COOKIE_NAME, "")
            .httpOnly(true)
            .secure(production)
            .path("/")
            .maxAge(0)
            .sameSite(production ? "None" : "Lax")
            .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    private boolean isProduction() {
        for (String profile : environment.getActiveProfiles()) {
            if ("prod".equalsIgnoreCase(profile)) {
                return true;
            }
        }
        return false;
    }
}
