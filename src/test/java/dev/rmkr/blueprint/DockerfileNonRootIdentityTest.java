package dev.rmkr.blueprint;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

class DockerfileNonRootIdentityTest {

    @Test
    void runtimeImageUsesFixedNonRootUidAndGid() throws Exception {
        String text = Files.readString(Path.of("Dockerfile"));
        assertThat(text).contains("groupadd --system --gid 10001 app");
        assertThat(text).contains("useradd --system --uid 10001 --gid app --no-create-home app");
        assertThat(text).contains("USER app");
    }
}
