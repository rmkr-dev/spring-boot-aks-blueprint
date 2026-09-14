# Observability

What this blueprint exposes today via Spring Boot Actuator and Micrometer. No Prometheus push, no APM agent, and no claim of a live dashboard.

## Endpoints (default)

| Path | Purpose |
| --- | --- |
| `/actuator/health` | Aggregate health |
| `/actuator/health/readiness` | Kubernetes readiness (probes enabled) |
| `/actuator/health/liveness` | Kubernetes liveness (probes enabled) |
| `/actuator/info` | App + blueprint metadata |
| `/actuator/metrics` | Micrometer metric names |
| `/actuator/metrics/{name}` | Single metric |
| `/actuator/prometheus` | Prometheus text exposition (Micrometer registry) |

Exposure is controlled in `src/main/resources/application.yml` (`management.endpoints.web.exposure.include`: `health`, `info`, `metrics`, `prometheus`). Sensitive endpoints such as `env` are **not** exposed.

## Custom metrics

`HelloService` registers:

| Metric | Type | Tags / notes |
| --- | --- | --- |
| `blueprint.hello.requests` | Counter | `outcome=default` when name blank/null; `outcome=named` otherwise |
| `blueprint.hello.duration` | Timer | Latency of producing a greeting |

After calling `GET /api/v1/hello`, inspect:

```bash
curl -s http://localhost:8080/actuator/metrics/blueprint.hello.requests
curl -s 'http://localhost:8080/actuator/metrics/blueprint.hello.requests?tag=outcome:named'
curl -s http://localhost:8080/actuator/metrics/blueprint.hello.duration
```

JVM and HTTP server metrics from Micrometer/Spring Boot remain available under `/actuator/metrics`. The `micrometer-registry-prometheus` dependency plus `management.prometheus.metrics.export.enabled=true` exposes the same meters at `/actuator/prometheus` in Prometheus text format. No remote-write or managed dashboard is configured in this blueprint.

## Info contributor

`BlueprintInfoContributor` adds a `blueprint` object to `/actuator/info` (stack label, Java version, purpose). Combined with the existing `info.app.*` properties from `application.yml`.

## Operations notes

- Prefer scraping `/actuator/prometheus` (or `/actuator/metrics`) from your platform—do not commit credentials for remote write. Sample pod annotations are in `deploy/k8s/deployment.yaml`.
- Kubernetes samples probe readiness/liveness paths; see [deployment.md](../deployment/deployment.md) and `deploy/k8s/`.
- Health details stay `when_authorized` so anonymous clients do not get component-level detail dumps.

Graceful shutdown (`server.shutdown=graceful`) is documented in [graceful-shutdown.md](graceful-shutdown.md). Request correlation via `X-Request-Id` is documented in [request-id.md](request-id.md).

## Out of scope here

- OpenTelemetry exporters, Grafana dashboards, Alertmanager rules, or Prometheus Operator ServiceMonitor CRs
- Changing Actuator base path or adding Spring Security

## HTTP response compression

`server.compression.enabled=true` with MIME types `application/json`, `application/problem+json`, and `text/plain` (min size 1024 bytes). Useful behind Ingress when clients accept gzip. Disable if an edge proxy already compresses end-to-end.
