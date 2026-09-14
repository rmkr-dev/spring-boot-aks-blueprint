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

The sample Deployment sets:

| Knob | Value | Role |
| --- | --- | --- |
| `terminationGracePeriodSeconds` | `45` | Kubelet wall-clock from Terminating until `SIGKILL` |
| container `lifecycle.preStop` | `sleep 5` | Delay `SIGTERM` so kube-proxy / EndpointSlice can drop the pod |

Budget: **5s preStop + 30s Spring drain + ~10s JVM-exit buffer = 45s**. Tune all three together: grace period must stay **greater than** preStop duration plus `timeout-per-shutdown-phase`.

Order of events on delete/roll:

1. Pod is marked Terminating; EndpointSlice removal starts (asynchronous)
2. `preStop` sleeps 5s so Service traffic can drain off this pod
3. Container receives `SIGTERM`
4. Spring Boot graceful Tomcat drain (≤ 30s); readiness will fail as the context shuts down
5. Process exits; if still running at 45s, kubelet sends `SIGKILL`

The short `sleep` does **not** replace `server.shutdown=graceful`. It only covers the race where kube-proxy still routes to a pod that has already started shutting down.

## Local check

```bash
mvn -B spring-boot:run
# elsewhere: curl traffic, then Ctrl+C — process should drain briefly instead of hard-killing
```

Or send `SIGTERM` to the JVM PID. No load generator is required for a smoke check.

## Out of scope

- Custom `TomcatServletWebServerFactory` beans
- Long preStop sleeps that *replace* graceful shutdown (the sample sleep is 5s only)
- Claiming zero dropped requests under extreme load without measurement

## Related Tomcat timeouts

This blueprint also sets:

| Property | Value | Role |
| --- | --- | --- |
| `server.tomcat.connection-timeout` | `10s` | Max time to accept/establish a connection |
| `server.tomcat.keep-alive-timeout` | `20s` | Idle keep-alive before the connector closes |

These are connector limits, separate from graceful shutdown's lifecycle timeout. Tune for your Ingress/LB idle timeouts so they stay aligned.
