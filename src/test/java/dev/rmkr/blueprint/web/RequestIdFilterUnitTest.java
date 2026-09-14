package dev.rmkr.blueprint.web;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RequestIdFilterUnitTest {

    private final RequestIdFilter filter = new RequestIdFilter();

    @AfterEach
    void clearMdc() {
        MDC.clear();
    }

    @Test
    void putsGeneratedIdInMdcDuringChain() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/v1/hello");
        MockHttpServletResponse response = new MockHttpServletResponse();
        AtomicReference<String> during = new AtomicReference<>();

        filter.doFilter(request, response, (req, res) -> during.set(MDC.get(RequestIdFilter.MDC_KEY)));

        String header = response.getHeader(RequestIdFilter.HEADER);
        assertEquals(header, during.get());
        UUID.fromString(header);
        assertNull(MDC.get(RequestIdFilter.MDC_KEY));
    }

    @Test
    void putsIncomingIdInMdcDuringChain() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/v1/hello");
        request.addHeader(RequestIdFilter.HEADER, " incoming-id ");
        MockHttpServletResponse response = new MockHttpServletResponse();
        AtomicReference<String> during = new AtomicReference<>();

        filter.doFilter(request, response, (req, res) -> during.set(MDC.get(RequestIdFilter.MDC_KEY)));

        assertEquals("incoming-id", during.get());
        assertEquals("incoming-id", response.getHeader(RequestIdFilter.HEADER));
        assertNull(MDC.get(RequestIdFilter.MDC_KEY));
    }

    @Test
    void removesMdcWhenChainThrowsServletException() {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/v1/hello");
        request.addHeader(RequestIdFilter.HEADER, "boom-id");
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = (req, res) -> {
            assertEquals("boom-id", MDC.get(RequestIdFilter.MDC_KEY));
            throw new ServletException("boom");
        };

        ServletException thrown = assertThrows(ServletException.class,
                () -> filter.doFilter(request, response, chain));
        assertEquals("boom", thrown.getMessage());
        assertEquals("boom-id", response.getHeader(RequestIdFilter.HEADER));
        assertNull(MDC.get(RequestIdFilter.MDC_KEY));
    }

    @Test
    void removesMdcWhenChainThrowsRuntimeException() {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/v1/hello");
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = (req, res) -> {
            assertTrue(MDC.get(RequestIdFilter.MDC_KEY) != null);
            throw new IllegalStateException("runtime-boom");
        };

        assertThrows(IllegalStateException.class, () -> filter.doFilter(request, response, chain));
        assertNull(MDC.get(RequestIdFilter.MDC_KEY));
    }

    @Test
    void removesMdcWhenChainThrowsIoException() {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/v1/hello");
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = (req, res) -> {
            throw new IOException("io-boom");
        };

        assertThrows(IOException.class, () -> filter.doFilter(request, response, chain));
        assertNull(MDC.get(RequestIdFilter.MDC_KEY));
    }

    @Test
    void usableIncomingRejectsNullBlankOversizeAndControls() {
        assertFalse(RequestIdFilter.usableIncoming(null));
        assertFalse(RequestIdFilter.usableIncoming(""));
        assertFalse(RequestIdFilter.usableIncoming("   "));
        assertFalse(RequestIdFilter.usableIncoming("a".repeat(RequestIdFilter.MAX_LENGTH + 1)));
        assertFalse(RequestIdFilter.usableIncoming("has\nnewline"));
        assertTrue(RequestIdFilter.usableIncoming("ok-id"));
        assertTrue(RequestIdFilter.usableIncoming("b".repeat(RequestIdFilter.MAX_LENGTH)));
    }
}
