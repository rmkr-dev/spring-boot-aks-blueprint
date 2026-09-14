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

    @Test
    void deploymentSetsMinReadySecondsForRollouts() throws Exception {
        String text = Files.readString(Path.of("deploy/k8s/deployment.yaml"));
        assertThat(text).contains("minReadySeconds: 10");
    }

    @Test
    void deploymentSetsProgressDeadlineSeconds() throws Exception {
        String text = Files.readString(Path.of("deploy/k8s/deployment.yaml"));
        assertThat(text).contains("progressDeadlineSeconds: 120");
    }
}
