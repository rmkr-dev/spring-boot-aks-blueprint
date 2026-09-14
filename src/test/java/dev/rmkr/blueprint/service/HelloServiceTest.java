package dev.rmkr.blueprint.service;

import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class HelloServiceTest {

    private SimpleMeterRegistry meterRegistry;
    private HelloService helloService;

    @BeforeEach
    void setUp() {
        meterRegistry = new SimpleMeterRegistry();
        helloService = new HelloService("spring-boot-aks-blueprint", meterRegistry);
    }

    @Test
    void greetDefaultsToWorld() {
        HelloService.HelloResponse response = helloService.greet(null);

        assertThat(response.message()).isEqualTo("Hello, world!");
        assertThat(response.application()).isEqualTo("spring-boot-aks-blueprint");
    }

    @Test
    void greetUsesProvidedName() {
        HelloService.HelloResponse response = helloService.greet("Ramkumar");

        assertThat(response.message()).isEqualTo("Hello, Ramkumar!");
    }

    @Test
    void greetTrimsBlankNameToWorld() {
        assertThat(helloService.greet("   ").message()).isEqualTo("Hello, world!");
    }

    @Test
    void greetTrimsSurroundingWhitespace() {
        assertThat(helloService.greet("  AKS  ").message()).isEqualTo("Hello, AKS!");
    }

    @Test
    void greetAcceptsEmptyStringAsWorld() {
        assertThat(helloService.greet("").message()).isEqualTo("Hello, world!");
    }

    @Test
    void greetAcceptsMaxLengthName() {
        String max = "n".repeat(64);
        assertThat(helloService.greet(max).message()).isEqualTo("Hello, " + max + "!");
    }

    @Test
    void greetPreservesInternalWhitespace() {
        assertThat(helloService.greet("hello world").message()).isEqualTo("Hello, hello world!");
    }

    @Test
    void greetReflectsConfiguredApplicationName() {
        HelloService other = new HelloService("custom-app", new SimpleMeterRegistry());
        assertThat(other.greet("x").application()).isEqualTo("custom-app");
    }

    @Test
    void greetTreatsTabOnlyAsBlank() {
        assertThat(helloService.greet("\t\t").message()).isEqualTo("Hello, world!");
    }

    @Test
    void greetIncrementsCustomCounter() {
        helloService.greet("one");
        helloService.greet("two");

        assertThat(meterRegistry.find("blueprint.hello.requests").counter())
                .isNotNull()
                .extracting(c -> c.count())
                .isEqualTo(2.0);
    }
}
