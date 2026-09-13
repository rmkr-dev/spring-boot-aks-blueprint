package dev.rmkr.blueprint.web;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ApiExceptionHandlerTest {

    private final ApiExceptionHandler handler = new ApiExceptionHandler();

    @Test
    void mapsConstraintViolationsToProblemDetail() {
        ConstraintViolation<?> violation = mock(ConstraintViolation.class);
        Path path = mock(Path.class);
        when(path.toString()).thenReturn("hello.name");
        when(violation.getPropertyPath()).thenReturn(path);
        when(violation.getMessage()).thenReturn("size must be between 0 and 64");

        ProblemDetail problem = handler.handleConstraintViolation(
                new ConstraintViolationException(Set.of(violation)));

        assertThat(problem.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(problem.getTitle()).isEqualTo("Bad Request");
        assertThat(problem.getDetail()).isEqualTo("Request validation failed");
        assertThat(problem.getProperties()).containsKey("errors");
        @SuppressWarnings("unchecked")
        var errors = (java.util.List<String>) problem.getProperties().get("errors");
        assertThat(errors).containsExactly("hello.name: size must be between 0 and 64");
    }
}
