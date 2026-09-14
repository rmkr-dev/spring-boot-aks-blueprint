package dev.rmkr.blueprint;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class CompressionConfigTest {

    @Autowired
    private Environment environment;

    @Test
    void compressionIsEnabled() {
        assertEquals("true", environment.getProperty("server.compression.enabled"));
        assertTrue(Boolean.parseBoolean(environment.getProperty("server.compression.enabled")));
    }

    @Test
    void compressionMimeTypesIncludeJson() {
        String mime = environment.getProperty("server.compression.mime-types");
        assertTrue(mime != null && mime.contains("application/json"));
    }
}
