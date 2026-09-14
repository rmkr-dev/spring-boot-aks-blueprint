package dev.rmkr.blueprint;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class GracefulShutdownConfigTest {

    @Autowired
    private Environment environment;

    @Test
    void serverShutdownIsGraceful() {
        assertThat(environment.getProperty("server.shutdown")).isEqualTo("graceful");
    }

    @Test
    void lifecycleShutdownTimeoutIsThirtySeconds() {
        assertThat(environment.getProperty("spring.lifecycle.timeout-per-shutdown-phase"))
                .isEqualTo("30s");
    }
}
