package eurcine.backend.config;

import eurcine.backend.security.JwtCookieAuthenticationFilter;
import eurcine.backend.security.RestAccessDeniedHandler;
import eurcine.backend.security.RestAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class SecurityConfig {
    private final Environment environment;
    private final String corsAllowedOrigins;
    private final JwtCookieAuthenticationFilter jwtCookieAuthenticationFilter;
    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;
    private final RestAccessDeniedHandler restAccessDeniedHandler;

    public SecurityConfig(Environment environment,
                          @Value("${app.cors.allowed-origins:}") String corsAllowedOrigins,
                          JwtCookieAuthenticationFilter jwtCookieAuthenticationFilter,
                          RestAuthenticationEntryPoint restAuthenticationEntryPoint,
                          RestAccessDeniedHandler restAccessDeniedHandler) {
        this.environment = environment;
        this.corsAllowedOrigins = corsAllowedOrigins;
        this.jwtCookieAuthenticationFilter = jwtCookieAuthenticationFilter;
        this.restAuthenticationEntryPoint = restAuthenticationEntryPoint;
        this.restAccessDeniedHandler = restAccessDeniedHandler;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(Customizer.withDefaults())
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/",
                    "/error",
                    "/swagger-ui.html",
                    "/swagger-ui/**",
                    "/v3/api-docs/**",
                    "/api/auth/login"
                ).permitAll()
                .requestMatchers("/api/auth/logout").authenticated()
                .requestMatchers("/api/auth/me").authenticated()
                .requestMatchers("/api/ordini/**").authenticated()
                .requestMatchers("/api/admin/**").hasAnyRole("ADMIN", "SUPER_ADMIN")
                .anyRequest().permitAll()
            )
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint(restAuthenticationEntryPoint)
                .accessDeniedHandler(restAccessDeniedHandler)
            )
            .httpBasic(httpBasic -> httpBasic.disable())
            .formLogin(form -> form.disable())
            .addFilterBefore(jwtCookieAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(resolveAllowedOrigins());
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(false);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    private List<String> resolveAllowedOrigins() {
        List<String> configuredOrigins = Arrays.stream(corsAllowedOrigins.split(","))
            .map(String::trim)
            .filter(origin -> !origin.isBlank())
            .collect(Collectors.toList());

        if (!configuredOrigins.isEmpty()) {
            return configuredOrigins;
        }

        if (List.of(environment.getActiveProfiles()).contains("prod")) {
            return List.of("https://web-site-eurcine-1.onrender.com");
        }

        return List.of("http://localhost:4200");
    }
}
