# Architecture

## Current state

This repository is a **Spring Boot + AKS engineering blueprint**. Application, container, CI, diagrams, ADR, reference Kubernetes manifests, and consistent API error handling are present. Nothing claims a live cluster apply.

| Surface | Role | Status |
| --- | --- | --- |
| `README.md` / `AGENTS.md` / `CONTRIBUTING.md` | Entry and guardrails | Present |
| `docs/*` | Architecture, security, development, deployment, ADRs | Present |
| `pom.xml` | Maven / Spring Boot 3.5 / Java 21 | Present |
| `src/main/java/dev/rmkr/blueprint` | App, `/api/v1/hello`, Actuator, ProblemDetail errors | Present |
| `src/test/java/...` | Unit + WebMvc + Actuator + error + graceful-shutdown config tests | Present |
| `Dockerfile` / `.dockerignore` / `compose.yaml` | Multi-stage non-root image + Compose healthcheck | Present |
| `.github/workflows/ci.yml` | Reusable Maven CI + docker build (no push) | Present |
| `scripts/` | Optional `load-hello` bash/Python smokes | Present |
| `deploy/k8s/` | Sample Deployment / Service / ConfigMap / HPA / NetworkPolicy / PDB / ServiceAccount / Ingress + `secret.example.yaml` | Reference only |

## Application components

```text
SpringBootAksBlueprintApplication
  └── HelloController (/api/v1/hello)
        └── HelloService  (message + spring.application.name)
  └── ApiExceptionHandler  (ConstraintViolation → ProblemDetail 400)
Actuator (exposed): /actuator/health(+readiness/liveness), /actuator/info, /actuator/metrics, /actuator/prometheus
  └── BlueprintInfoContributor → info.blueprint
HelloService counter: blueprint.hello.requests
```

See the Mermaid view in [architecture-diagram.md](architecture-diagram.md).

Configuration prefers environment variables over hardcoding (`SERVER_PORT`, `SPRING_APPLICATION_NAME`). Health probes are enabled for Kubernetes samples; health details are not shown anonymously. There is no authentication layer and no datastore in this blueprint. Springdoc/OpenAPI is deferred—see [api-errors.md](../development/api-errors.md).

## Network posture

Local and container processes listen on port 8080. Sample Kubernetes Service/Pod/Ingress networking is documented as **reference**; a live Azure LB/TLS endpoint remains operator-owned. See [network-diagram.md](network-diagram.md).

## Related decisions

- [ADR-001: Spring Boot + AKS blueprint](../decisions/0001-spring-boot-aks-blueprint.md)

## What is intentionally out of scope here

- Claiming a production AKS deployment
- Auth, databases, or multi-service meshes
- Registry push or `kubectl apply` from CI
- springdoc-openapi until the public API surface justifies it
