# Docker HEALTHCHECK

The multi-stage `Dockerfile` includes a `HEALTHCHECK` aligned with Actuator liveness used by the sample Kubernetes probes.

| Setting | Value |
| --- | --- |
| Endpoint | `http://127.0.0.1:${SERVER_PORT}/actuator/health/liveness` |
| Interval | 30s |
| Timeout | 3s |
| Start period | 45s (JVM warm-up) |
| Retries | 3 |

`curl` is installed in the runtime stage so the healthcheck can run without a custom Java probe. Compose and `docker run` report container health from this check; Kubernetes still prefers the HTTP probes in `deploy/k8s/deployment.yaml`.

This is local/container convenience—not proof of a live AKS deployment.

## Compose

`compose.yaml` mirrors the same Actuator liveness check under `services.app.healthcheck` so `docker compose ps` can show healthy/unhealthy without a separate probe definition.
