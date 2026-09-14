package dev.rmkr.blueprint;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class VirtualThreadsConfigTest {

    @Autowired
    private Environment environment;

    @Test
    void virtualThreadsAreEnabled() {
        assertEquals("true", environment.getProperty("spring.threads.virtual.enabled"));
        assertTrue(Boolean.parseBoolean(environment.getProperty("spring.threads.virtual.enabled")));
    }
}
