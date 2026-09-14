# Request ID correlation

## What ships

`RequestIdFilter` ensures every HTTP response includes an `X-Request-Id` header:

- If the client sends `X-Request-Id`, the value is trimmed and echoed.
- Otherwise a UUID is generated.
- The same value is placed in SLF4J MDC under `requestId` for the duration of the request.
- `src/main/resources/logback-spring.xml` includes `%X{requestId:-}` in the console pattern so correlation ids appear in stdout when present.

This is **correlation**, not distributed tracing. No OpenTelemetry exporter or Zipkin/Jaeger sink is configured.

## Verify locally

```bash
curl -sI http://localhost:8080/api/v1/hello | grep -i x-request-id
curl -sI -H 'X-Request-Id: demo-1' http://localhost:8080/api/v1/hello | grep -i x-request-id
```

With the app running, look for `[demo-1]` (or a UUID) near each log line after a request that set or generated the header.

## Out of scope

- W3C `traceparent` propagation
- OpenTelemetry / Zipkin / Jaeger exporters
- Claiming cross-service tracing from this single-process filter
