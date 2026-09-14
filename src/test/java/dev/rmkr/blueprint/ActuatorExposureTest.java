package dev.rmkr.blueprint;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ActuatorExposureTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void healthEndpointIsExposed() throws Exception {
        mockMvc.perform(get("/actuator/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"));
    }

    @Test
    void readinessProbeEndpointIsExposed() throws Exception {
        mockMvc.perform(get("/actuator/health/readiness"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"));
    }

    @Test
    void livenessProbeEndpointIsExposed() throws Exception {
        mockMvc.perform(get("/actuator/health/liveness"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"));
    }

    @Test
    void metricsEndpointIsExposed() throws Exception {
        mockMvc.perform(get("/actuator/metrics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.names").isArray());
    }

    @Test
    void infoEndpointIsExposed() throws Exception {
        mockMvc.perform(get("/actuator/info"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.app.name").value("spring-boot-aks-blueprint"))
                .andExpect(jsonPath("$.blueprint.stack").value("spring-boot-aks"));
    }

    @Test
    void envEndpointIsNotExposed() throws Exception {
        mockMvc.perform(get("/actuator/env"))
                .andExpect(status().isNotFound());
    }

    @Test
    void customHelloMetricAppearsAfterGreeting() throws Exception {
        mockMvc.perform(get("/api/v1/hello"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/actuator/metrics/blueprint.hello.requests"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("blueprint.hello.requests"))
                .andExpect(jsonPath("$.measurements[0].value").value(Matchers.greaterThanOrEqualTo(1.0)));
    }

    @Test
    void helloDurationTimerAppearsAfterGreeting() throws Exception {
        mockMvc.perform(get("/api/v1/hello").param("name", "timer"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/actuator/metrics/blueprint.hello.duration"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("blueprint.hello.duration"))
                .andExpect(jsonPath("$.measurements").isArray());
    }


    @Test
    void helloRequestMetricSupportsOutcomeTagFilter() throws Exception {
        mockMvc.perform(get("/api/v1/hello").param("name", "tagged"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/actuator/metrics/blueprint.hello.requests")
                        .param("tag", "outcome:named"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("blueprint.hello.requests"))
                .andExpect(jsonPath("$.measurements[0].value").value(Matchers.greaterThanOrEqualTo(1.0)));
    }
}
