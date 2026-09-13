package dev.rmkr.blueprint.web;

import dev.rmkr.blueprint.service.HelloService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HelloController.class)
@Import(HelloService.class)
@TestPropertySource(properties = "spring.application.name=spring-boot-aks-blueprint")
class HelloControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void helloReturnsDefaultGreeting() throws Exception {
        mockMvc.perform(get("/api/v1/hello"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Hello, world!"))
                .andExpect(jsonPath("$.application").value("spring-boot-aks-blueprint"));
    }

    @Test
    void helloAcceptsNameQueryParam() throws Exception {
        mockMvc.perform(get("/api/v1/hello").param("name", "AKS"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Hello, AKS!"));
    }
}
