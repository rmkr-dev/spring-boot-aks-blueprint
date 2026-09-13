# API errors

## Current behavior

Successful responses from `GET /api/v1/hello` return JSON:

```json
{ "message": "Hello, world!", "application": "spring-boot-aks-blueprint" }
```

Validation failures (for example `name` longer than 64 characters) are handled by `ApiExceptionHandler` and return **RFC 7807 Problem Details** (`application/problem+json`) with HTTP 400:

```json
{
  "type": "about:blank",
  "title": "Bad Request",
  "status": 400,
  "detail": "Request validation failed",
  "errors": ["hello.name: size must be between 0 and 64"]
}
```

Advice for extending this blueprint:

- Prefer `ProblemDetail` (or a thin wrapper) over ad-hoc error maps.
- Keep field-level messages in a stable property such as `errors`.
- Do not leak stack traces or internal exception types to clients.
- Map domain “not found” / conflict cases explicitly when you add more resources.

## OpenAPI / Springdoc

OpenAPI generation via **springdoc-openapi** is **intentionally deferred**. The sample API is one GET endpoint; adding springdoc would increase dependency surface without enough payload yet. When you grow the public API, reconsider a minimal springdoc setup in its own PR with tests—do not add it “just in case.”
