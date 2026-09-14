# Graceful shutdown

How this blueprint drains HTTP work before the JVM exits. Useful for Kubernetes rolling updates and local `SIGTERM`.

## Application config

In `src/main/resources/application.yml`:

| Property | Value | Role |
| --- | --- | --- |
| `server.shutdown` | `graceful` | Stop accepting new requests; finish in-flight ones |
| `spring.lifecycle.timeout-per-shutdown-phase` | `30s` | Max wait for the web server (and other lifecycle beans) to stop |

Without `server.shutdown=graceful`, Tomcat closes connections immediately on stop. With it, Spring Boot pauses the connector and waits up to the lifecycle timeout.

## Kubernetes alignment

The sample Deployment sets `terminationGracePeriodSeconds: 45` so the kubelet allows more wall-clock time than the 30s Spring phase (buffer for SIGTERM delivery and JVM exit). Order of events on delete/roll:

1. Pod receives `SIGTERM`
2. Readiness fails (endpoint removed from Service) as the process shuts down
3. Graceful Tomcat drain (≤ 30s)
4. Process exits; if still running at 45s, kubelet sends `SIGKILL`

Tune both values together: grace period should be **greater than** `timeout-per-shutdown-phase`.

## Local check

```bash
mvn -B spring-boot:run
# elsewhere: curl traffic, then Ctrl+C — process should drain briefly instead of hard-killing
```

Or send `SIGTERM` to the JVM PID. No load generator is required for a smoke check.

## Out of scope

- Custom `TomcatServletWebServerFactory` beans
- PreStop hooks that sleep instead of relying on graceful shutdown (optional later)
- Claiming zero dropped requests under extreme load without measurement

## Related Tomcat timeouts

This blueprint also sets:

| Property | Value | Role |
| --- | --- | --- |
| `server.tomcat.connection-timeout` | `10s` | Max time to accept/establish a connection |
| `server.tomcat.keep-alive-timeout` | `20s` | Idle keep-alive before the connector closes |

These are connector limits, separate from graceful shutdown's lifecycle timeout. Tune for your Ingress/LB idle timeouts so they stay aligned.
