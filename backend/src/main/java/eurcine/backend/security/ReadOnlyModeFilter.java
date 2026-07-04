package eurcine.backend.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

public class ReadOnlyModeFilter extends OncePerRequestFilter {

    private final boolean readOnlyMode;
    private final ObjectMapper objectMapper;

    public ReadOnlyModeFilter(
        @Value("${app.read-only-mode:false}") boolean readOnlyMode,
        ObjectMapper objectMapper
    ) {
        this.readOnlyMode = readOnlyMode;
        this.objectMapper = objectMapper;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        if (!readOnlyMode) {
            return true;
        }

        String method = request.getMethod();
        return HttpMethod.GET.matches(method)
            || HttpMethod.HEAD.matches(method)
            || HttpMethod.OPTIONS.matches(method);
    }

    @Override
    protected boolean shouldNotFilterAsyncDispatch() {
        return true;
    }

    @Override
    protected boolean shouldNotFilterErrorDispatch() {
        return true;
    }

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {
        if (!readOnlyMode) {
            filterChain.doFilter(request, response);
            return;
        }

        response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("status", HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        body.put("message", "Prod read-only mode attivo: questa operazione e' disabilitata.");

        objectMapper.writeValue(response.getOutputStream(), body);
    }
}
