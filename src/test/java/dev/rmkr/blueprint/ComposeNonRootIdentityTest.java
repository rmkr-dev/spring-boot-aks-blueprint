package dev.rmkr.blueprint;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

class ComposeNonRootIdentityTest {

    @Test
    void composeRunsAsFixedNonRootUidAndGid() throws Exception {
        String text = Files.readString(Path.of("compose.yaml"));
        assertThat(text).contains("user: \"10001:10001\"");
    }
}
