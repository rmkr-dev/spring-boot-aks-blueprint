package dev.rmkr.blueprint.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityHeadersFilterTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void helloResponseIncludesSecurityHeaders() throws Exception {
        expectAllSecurityHeaders(mockMvc.perform(get("/api/v1/hello"))
                .andExpect(status().isOk()));
    }

    @Test
    void healthResponseIncludesSecurityHeaders() throws Exception {
        mockMvc.perform(get("/actuator/health"))
                .andExpect(status().isOk())
                .andExpect(header().string("X-Content-Type-Options", "nosniff"))
                .andExpect(header().string("X-Frame-Options", "DENY"));
    }

    @Test
    void prometheusResponseIncludesAllSecurityHeaders() throws Exception {
        expectAllSecurityHeaders(mockMvc.perform(get("/actuator/prometheus"))
                .andExpect(status().isOk()));
    }

    @Test
    void infoResponseIncludesAllSecurityHeaders() throws Exception {
        expectAllSecurityHeaders(mockMvc.perform(get("/actuator/info"))
                .andExpect(status().isOk()));
    }

    @Test
    void validationErrorIncludesAllSecurityHeaders() throws Exception {
        expectAllSecurityHeaders(mockMvc.perform(get("/api/v1/hello").param("name", "x".repeat(65)))
                .andExpect(status().isBadRequest()));
    }

    @Test
    void notFoundIncludesAllSecurityHeaders() throws Exception {
        expectAllSecurityHeaders(mockMvc.perform(get("/api/v1/missing"))
                .andExpect(status().isNotFound()));
    }

    @Test
    void helloResponseIncludesRequestIdAndSecurityHeaders() throws Exception {
        expectAllSecurityHeaders(mockMvc.perform(get("/api/v1/hello")
                        .header(RequestIdFilter.HEADER, "stack-1"))
                .andExpect(status().isOk())
                .andExpect(header().string(RequestIdFilter.HEADER, "stack-1")));
    }

    private static void expectAllSecurityHeaders(ResultActions actions) throws Exception {
        actions.andExpect(header().string("X-Content-Type-Options", "nosniff"))
                .andExpect(header().string("X-Frame-Options", "DENY"))
                .andExpect(header().string("Referrer-Policy", "no-referrer"))
                .andExpect(header().string("X-XSS-Protection", "0"))
                .andExpect(header().string("Permissions-Policy", "geolocation=(), microphone=(), camera=()"));
    }
}
