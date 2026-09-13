package dev.rmkr.blueprint.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class HelloServiceTest {

    private HelloService helloService;

    @BeforeEach
    void setUp() {
        helloService = new HelloService("spring-boot-aks-blueprint");
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
}
