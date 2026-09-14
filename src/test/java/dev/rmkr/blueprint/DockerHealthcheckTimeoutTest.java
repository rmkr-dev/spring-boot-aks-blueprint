package dev.rmkr.blueprint;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

class DockerHealthcheckTimeoutTest {

    @Test
    void dockerfileHealthcheckCapsCurlRuntime() throws Exception {
        String text = Files.readString(Path.of("Dockerfile"));
        assertThat(text).contains("curl -fsS --max-time 2");
        assertThat(text).contains("--timeout=3s");
    }

    @Test
    void composeHealthcheckCapsCurlRuntime() throws Exception {
        String text = Files.readString(Path.of("compose.yaml"));
        assertThat(text).contains("\"--max-time\"");
        assertThat(text).contains("\"2\"");
    }
}
