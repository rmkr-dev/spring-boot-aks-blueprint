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

    @Test
    void sortsMultipleViolationsLexicographically() {
        ConstraintViolation<?> a = mock(ConstraintViolation.class);
        Path pathA = mock(Path.class);
        when(pathA.toString()).thenReturn("hello.z");
        when(a.getPropertyPath()).thenReturn(pathA);
        when(a.getMessage()).thenReturn("must not be blank");

        ConstraintViolation<?> b = mock(ConstraintViolation.class);
        Path pathB = mock(Path.class);
        when(pathB.toString()).thenReturn("hello.a");
        when(b.getPropertyPath()).thenReturn(pathB);
        when(b.getMessage()).thenReturn("size must be between 0 and 64");

        ProblemDetail problem = handler.handleConstraintViolation(
                new ConstraintViolationException(Set.of(a, b)));

        @SuppressWarnings("unchecked")
        var errors = (java.util.List<String>) problem.getProperties().get("errors");
        assertThat(errors).containsExactly(
                "hello.a: size must be between 0 and 64",
                "hello.z: must not be blank");
    }
}
