package dev.rmkr.blueprint;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class ForwardHeadersConfigTest {

    @Autowired
    private Environment environment;

    @Test
    void forwardHeadersStrategyIsFramework() {
        assertEquals("framework", environment.getProperty("server.forward-headers-strategy"));
    }
}
