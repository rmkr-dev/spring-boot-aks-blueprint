package dev.rmkr.blueprint.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class HelloService {

    private final String applicationName;

    public HelloService(@Value("${spring.application.name}") String applicationName) {
        this.applicationName = applicationName;
    }

    public HelloResponse greet(String name) {
        String who = (name == null || name.isBlank()) ? "world" : name.trim();
        return new HelloResponse("Hello, " + who + "!", applicationName);
    }

    public record HelloResponse(String message, String application) {
    }
}
