package dev.rmkr.blueprint;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BuildInfoContributorTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired(required = false)
    private BuildProperties buildProperties;

    @Test
    void buildPropertiesAreAvailable() {
        assertNotNull(buildProperties, "BuildProperties should be produced by spring-boot:build-info");
        assertEquals("spring-boot-aks-blueprint", buildProperties.getArtifact());
        assertEquals("1.3.1", buildProperties.getVersion());
    }

    @Test
    void actuatorInfoExposesBuildSection() throws Exception {
        mockMvc.perform(get("/actuator/info"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.build.artifact").value("spring-boot-aks-blueprint"))
                .andExpect(jsonPath("$.build.version").value("1.3.1"));
    }
}
