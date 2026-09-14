package dev.rmkr.blueprint.web;

import dev.rmkr.blueprint.service.HelloService;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HelloController.class)
@Import({HelloService.class, ApiExceptionHandler.class, HelloControllerTest.MeterConfig.class})
@TestPropertySource(properties = "spring.application.name=spring-boot-aks-blueprint")
class HelloControllerTest {

    @Autowired
    private MockMvc mockMvc;

    static class MeterConfig {
        @Bean
        SimpleMeterRegistry simpleMeterRegistry() {
            return new SimpleMeterRegistry();
        }
    }

    @Test
    void helloReturnsDefaultGreeting() throws Exception {
        mockMvc.perform(get("/api/v1/hello"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Hello, world!"))
                .andExpect(jsonPath("$.application").value("spring-boot-aks-blueprint"));
    }

    @Test
    void helloAcceptsNameQueryParam() throws Exception {
        mockMvc.perform(get("/api/v1/hello").param("name", "AKS"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Hello, AKS!"));
    }

    @Test
    void helloTreatsBlankNameAsWorld() throws Exception {
        mockMvc.perform(get("/api/v1/hello").param("name", "   "))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Hello, world!"));
    }

    @Test
    void helloAcceptsNameAtMaxLength() throws Exception {
        String max = "a".repeat(64);
        mockMvc.perform(get("/api/v1/hello").param("name", max))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Hello, " + max + "!"));
    }

    @Test
    void helloTrimsSurroundingWhitespaceViaService() throws Exception {
        mockMvc.perform(get("/api/v1/hello").param("name", "  blueprint  "))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Hello, blueprint!"));
    }

    @Test
    void helloRejectsOversizedNameWithProblemDetail() throws Exception {
        String oversized = "x".repeat(65);
        mockMvc.perform(get("/api/v1/hello").param("name", oversized))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value("Bad Request"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.detail").value("Request validation failed"))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors", hasItem(containsString("size must be between 0 and 64"))));
    }

    @Test
    void unknownApiPathIsNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/missing"))
                .andExpect(status().isNotFound());
    }
}
