package dev.rmkr.blueprint;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

class KubernetesGracefulShutdownManifestTest {

    @Test
    void deploymentPairsGracePeriodWithPreStopSleep() throws Exception {
        Path yaml = Path.of("deploy/k8s/deployment.yaml");
        assertThat(yaml).exists();
        String text = Files.readString(yaml);
        assertThat(text).contains("terminationGracePeriodSeconds: 45");
        assertThat(text).contains("preStop:");
        assertThat(text).contains("command: [\"sleep\", \"5\"]");
    }
}
