package dev.rmkr.blueprint.web;

import dev.rmkr.blueprint.service.HelloService;
import jakarta.validation.constraints.Size;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@Validated
public class HelloController {

    private final HelloService helloService;

    public HelloController(HelloService helloService) {
        this.helloService = helloService;
    }

    @GetMapping("/hello")
    public HelloService.HelloResponse hello(
            @RequestParam(name = "name", required = false)
            @Size(max = 64) String name) {
        return helloService.greet(name);
    }
}
