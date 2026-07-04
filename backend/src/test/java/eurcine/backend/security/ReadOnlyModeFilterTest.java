package eurcine.backend.security;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import java.util.concurrent.atomic.AtomicBoolean;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

class ReadOnlyModeFilterTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void blocksMutatingRequestsWhenEnabled() throws Exception {
        ReadOnlyModeFilter filter = new ReadOnlyModeFilter(true, objectMapper);
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/api/auth/login");
        MockHttpServletResponse response = new MockHttpServletResponse();
        AtomicBoolean chainInvoked = new AtomicBoolean(false);
        FilterChain chain = (ServletRequest servletRequest, ServletResponse servletResponse) -> chainInvoked.set(true);

        filter.doFilterInternal(request, response, chain);

        assertThat(chainInvoked).isFalse();
        assertThat(response.getStatus()).isEqualTo(405);
        assertThat(response.getContentType()).isEqualTo("application/json");
        assertThat(response.getContentAsString()).contains("Prod read-only mode");
    }

    @Test
    void allowsSafeRequestsWhenEnabled() throws Exception {
        ReadOnlyModeFilter filter = new ReadOnlyModeFilter(true, objectMapper);
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/film");
        MockHttpServletResponse response = new MockHttpServletResponse();
        AtomicBoolean chainInvoked = new AtomicBoolean(false);
        FilterChain chain = (ServletRequest servletRequest, ServletResponse servletResponse) -> {
            chainInvoked.set(true);
            response.setStatus(200);
        };

        filter.doFilter(request, response, chain);

        assertThat(chainInvoked).isTrue();
        assertThat(response.getStatus()).isEqualTo(200);
    }

    @Test
    void staysTransparentWhenDisabled() throws Exception {
        ReadOnlyModeFilter filter = new ReadOnlyModeFilter(false, objectMapper);
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/api/auth/login");
        MockHttpServletResponse response = new MockHttpServletResponse();
        AtomicBoolean chainInvoked = new AtomicBoolean(false);
        FilterChain chain = (ServletRequest servletRequest, ServletResponse servletResponse) -> {
            chainInvoked.set(true);
            response.setStatus(204);
        };

        filter.doFilter(request, response, chain);

        assertThat(chainInvoked).isTrue();
        assertThat(response.getStatus()).isEqualTo(204);
    }
}
