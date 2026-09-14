package dev.rmkr.blueprint;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BuildInfoContributorTest {

    private static final Pattern VERSION = Pattern.compile("<version>([^<]+)</version>");

    @Autowired
    private MockMvc mockMvc;

    @Autowired(required = false)
    private BuildProperties buildProperties;

    /**
     * Project version is the first {@code <version>} after {@code </parent>} in {@code pom.xml}.
     * Avoids hardcoding so release bumps do not break this test.
     */
    static String pomProjectVersion() throws Exception {
        String pom = Files.readString(Path.of("pom.xml"));
        int parentEnd = pom.indexOf("</parent>");
        if (parentEnd < 0) {
            throw new IllegalStateException("pom.xml missing </parent>");
        }
        Matcher m = VERSION.matcher(pom.substring(parentEnd));
        if (!m.find()) {
            throw new IllegalStateException("project version not found in pom.xml");
        }
        return m.group(1).trim();
    }

    @Test
    void buildPropertiesAreAvailable() throws Exception {
        assertNotNull(buildProperties, "BuildProperties should be produced by spring-boot:build-info");
        assertEquals("spring-boot-aks-blueprint", buildProperties.getArtifact());
        assertEquals(pomProjectVersion(), buildProperties.getVersion());
    }

    @Test
    void actuatorInfoExposesBuildSection() throws Exception {
        String version = pomProjectVersion();
        mockMvc.perform(get("/actuator/info"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.build.artifact").value("spring-boot-aks-blueprint"))
                .andExpect(jsonPath("$.build.version").value(version));
    }
}
