package dev.rmkr.blueprint.service;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class HelloService {

    private final String applicationName;
    private final Counter helloRequests;

    public HelloService(
            @Value("${spring.application.name}") String applicationName,
            MeterRegistry meterRegistry) {
        this.applicationName = applicationName;
        this.helloRequests = Counter.builder("blueprint.hello.requests")
                .description("Number of hello greetings produced")
                .register(meterRegistry);
    }

    public HelloResponse greet(String name) {
        String who = (name == null || name.isBlank()) ? "world" : name.trim();
        helloRequests.increment();
        return new HelloResponse("Hello, " + who + "!", applicationName);
    }

    public record HelloResponse(String message, String application) {
    }
}
