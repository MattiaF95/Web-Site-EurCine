package eurcine.backend.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;
import eurcine.backend.dto.AuthMeResponse;
import eurcine.backend.dto.LoginRequest;
import eurcine.backend.dto.LoginResponse;
import eurcine.backend.dto.RegisterRequest;
import eurcine.backend.service.AuthService;
import eurcine.backend.service.JwtService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        AuthService.LoginResult loginResult = authService.loginWithJwt(request);
        return loginResult.response();
    }

    @PostMapping("/register")
    public LoginResponse register(@Valid @RequestBody RegisterRequest request) {
        AuthService.LoginResult registerResult = authService.registerWithJwt(request);
        return registerResult.response();
    }

    @GetMapping("/me")
    public AuthMeResponse me(@AuthenticationPrincipal JwtService.UserPrincipal user) {
        return authService.me(user);
    }

    @PostMapping("/logout")
    public void logout() {
        // Stateless JWT bearer logout is handled client-side by discarding token.
    }
}
