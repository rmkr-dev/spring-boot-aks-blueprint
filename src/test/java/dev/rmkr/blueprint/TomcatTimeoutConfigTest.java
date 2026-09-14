package dev.rmkr.blueprint;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class TomcatTimeoutConfigTest {

    @Autowired
    private Environment environment;

    @Test
    void connectionTimeoutIsConfigured() {
        assertEquals("10s", environment.getProperty("server.tomcat.connection-timeout"));
    }

    @Test
    void keepAliveTimeoutIsConfigured() {
        assertEquals("20s", environment.getProperty("server.tomcat.keep-alive-timeout"));
    }
}
