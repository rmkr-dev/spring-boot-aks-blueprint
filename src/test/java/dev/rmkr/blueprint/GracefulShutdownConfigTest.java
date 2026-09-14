package dev.rmkr.blueprint;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.context.LifecycleProperties;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.web.server.Shutdown;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class GracefulShutdownConfigTest {

    @Autowired
    private Environment environment;

    @Autowired
    private ServerProperties serverProperties;

    @Autowired
    private LifecycleProperties lifecycleProperties;

    @Test
    void serverShutdownIsGraceful() {
        assertThat(environment.getProperty("server.shutdown")).isEqualTo("graceful");
    }

    @Test
    void lifecycleShutdownTimeoutIsThirtySeconds() {
        assertThat(environment.getProperty("spring.lifecycle.timeout-per-shutdown-phase"))
                .isEqualTo("30s");
    }

    @Test
    void serverPropertiesBindGracefulShutdown() {
        assertThat(serverProperties.getShutdown()).isEqualTo(Shutdown.GRACEFUL);
    }

    @Test
    void lifecyclePropertiesBindThirtySecondPhaseTimeout() {
        assertThat(lifecycleProperties.getTimeoutPerShutdownPhase()).isEqualTo(Duration.ofSeconds(30));
    }
}
