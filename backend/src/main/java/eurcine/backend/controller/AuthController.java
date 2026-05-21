package eurcine.backend.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
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

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request, HttpServletResponse response) {
        AuthService.LoginResult loginResult = authService.loginWithSession(request);

        ResponseCookie cookie = ResponseCookie.from(SESSION_COOKIE_NAME, loginResult.sessionToken())
            .httpOnly(true)
            .secure(false)
            .path("/")
            .maxAge(7 * 24 * 60 * 60)
            .sameSite("Lax")
            .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return loginResult.response();
    }

    @GetMapping("/me")
    public AuthMeResponse me(@CookieValue(name = SESSION_COOKIE_NAME, required = false) String sessionToken) {
        return authService.me(sessionToken);
    }

    @PostMapping("/logout")
    public void logout(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from(SESSION_COOKIE_NAME, "")
            .httpOnly(true)
            .secure(false)
            .path("/")
            .maxAge(0)
            .sameSite("Lax")
            .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }
}
