# Request ID correlation

## What ships

`RequestIdFilter` ensures every HTTP response includes an `X-Request-Id` header:

- If the client sends `X-Request-Id`, the value is trimmed and echoed.
- Otherwise a UUID is generated.
- The same value is placed in SLF4J MDC under `requestId` for the duration of the request.

This is **correlation**, not distributed tracing. No OpenTelemetry exporter or Zipkin/Jaeger sink is configured.

## Verify locally

```bash
curl -sI http://localhost:8080/api/v1/hello | grep -i x-request-id
curl -sI -H 'X-Request-Id: demo-1' http://localhost:8080/api/v1/hello | grep -i x-request-id
```

## Out of scope

- W3C `traceparent` propagation
- Changing log patterns to print `%X{requestId}` in a custom `logback-spring.xml` (default Boot logging still benefits when you add it later)
