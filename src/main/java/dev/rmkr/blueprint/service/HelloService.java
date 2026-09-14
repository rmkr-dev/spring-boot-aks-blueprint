package dev.rmkr.blueprint.service;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class HelloService {

    private final String applicationName;
    private final MeterRegistry meterRegistry;
    private final Timer helloTimer;

    public HelloService(
            @Value("${spring.application.name}") String applicationName,
            MeterRegistry meterRegistry) {
        this.applicationName = applicationName;
        this.meterRegistry = meterRegistry;
        this.helloTimer = Timer.builder("blueprint.hello.duration")
                .description("Time to produce a hello greeting")
                .register(meterRegistry);
    }

    public HelloResponse greet(String name) {
        return helloTimer.record(() -> {
            boolean named = name != null && !name.isBlank();
            String who = named ? name.trim() : "world";
            Counter.builder("blueprint.hello.requests")
                    .description("Number of hello greetings produced")
                    .tag("outcome", named ? "named" : "default")
                    .register(meterRegistry)
                    .increment();
            return new HelloResponse("Hello, " + who + "!", applicationName);
        });
    }

    public record HelloResponse(String message, String application) {
    }
}
