package eurcine.backend.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.core.env.Environment;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import eurcine.backend.dto.AuthMeResponse;
import eurcine.backend.dto.LoginRequest;
import eurcine.backend.dto.LoginResponse;
import eurcine.backend.dto.RegisterRequest;
import eurcine.backend.service.AuthService;
import eurcine.backend.service.JwtService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final String SESSION_COOKIE_NAME = "eurcine_session";
    private static final String SESSION_HINT_COOKIE_NAME = "eurcine_session_present";
    private final AuthService authService;
    private final Environment environment;

    public AuthController(AuthService authService, Environment environment) {
        this.authService = authService;
        this.environment = environment;
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request, HttpServletResponse response) {
        AuthService.LoginResult loginResult = authService.loginWithJwt(request);
        applySessionCookies(response, loginResult.jwtToken());
        return loginResult.response();
    }

    @PostMapping("/register")
    public LoginResponse register(@RequestBody RegisterRequest request, HttpServletResponse response) {
        AuthService.LoginResult registerResult = authService.registerWithJwt(request);
        applySessionCookies(response, registerResult.jwtToken());
        return registerResult.response();
    }

    @GetMapping("/me")
    public AuthMeResponse me(@AuthenticationPrincipal JwtService.UserPrincipal user) {
        return authService.me(user);
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
        ResponseCookie hintCookie = ResponseCookie.from(SESSION_HINT_COOKIE_NAME, "")
            .httpOnly(false)
            .secure(production)
            .path("/")
            .maxAge(0)
            .sameSite(production ? "None" : "Lax")
            .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, hintCookie.toString());
    }

    private void applySessionCookies(HttpServletResponse response, String jwtToken) {
        boolean production = isProduction();
        ResponseCookie cookie = ResponseCookie.from(SESSION_COOKIE_NAME, jwtToken)
            .httpOnly(true)
            .secure(production)
            .path("/")
            .maxAge(7 * 24 * 60 * 60)
            .sameSite(production ? "None" : "Lax")
            .build();
        ResponseCookie hintCookie = ResponseCookie.from(SESSION_HINT_COOKIE_NAME, "1")
            .httpOnly(false)
            .secure(production)
            .path("/")
            .maxAge(7 * 24 * 60 * 60)
            .sameSite(production ? "None" : "Lax")
            .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, hintCookie.toString());
    }

    private boolean isProduction() {
        for (String profile : environment.getActiveProfiles()) {
            if ("prod".equalsIgnoreCase(profile)) {
                return true;
            }
        }
        String renderFlag = System.getenv("RENDER");
        if (renderFlag != null && !renderFlag.isBlank()) {
            return true;
        }
        String renderExternalUrl = System.getenv("RENDER_EXTERNAL_URL");
        return renderExternalUrl != null && !renderExternalUrl.isBlank();
    }
}
