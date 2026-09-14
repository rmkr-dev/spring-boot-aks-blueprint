package dev.rmkr.blueprint.web;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SecurityHeadersFilterUnitTest {

    private final SecurityHeadersFilter filter = new SecurityHeadersFilter();

    @Test
    void setsConservativeHeadersBeforeChainProceeds() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/v1/hello");
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = (req, res) -> {
            assertEquals("nosniff", response.getHeader("X-Content-Type-Options"));
            assertEquals("DENY", response.getHeader("X-Frame-Options"));
            assertEquals("no-referrer", response.getHeader("Referrer-Policy"));
            assertEquals("0", response.getHeader("X-XSS-Protection"));
            assertEquals("geolocation=(), microphone=(), camera=()", response.getHeader("Permissions-Policy"));
            assertEquals("no-store", response.getHeader("Cache-Control"));
        };

        filter.doFilter(request, response, chain);
        assertEquals("nosniff", response.getHeader("X-Content-Type-Options"));
        assertEquals("no-store", response.getHeader("Cache-Control"));
    }

    @Test
    void headersRemainWhenChainThrows() {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/actuator/health");
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = (req, res) -> {
            throw new ServletException("downstream");
        };

        assertThrows(ServletException.class, () -> filter.doFilter(request, response, chain));
        assertEquals("nosniff", response.getHeader("X-Content-Type-Options"));
        assertEquals("DENY", response.getHeader("X-Frame-Options"));
        assertEquals("no-referrer", response.getHeader("Referrer-Policy"));
        assertEquals("0", response.getHeader("X-XSS-Protection"));
        assertEquals("geolocation=(), microphone=(), camera=()", response.getHeader("Permissions-Policy"));
        assertEquals("no-store", response.getHeader("Cache-Control"));
    }
}
