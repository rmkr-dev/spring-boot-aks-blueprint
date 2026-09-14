# Architecture diagram

Mermaid view of components **that exist in the tree**. AKS appears only as a documented target.

```mermaid
flowchart TB
  subgraph jvm["Implemented: Spring Boot process"]
    App["SpringBootAksBlueprintApplication"]
    Ctrl["HelloController<br/>GET /api/v1/hello"]
    Svc["HelloService"]
    Err["ApiExceptionHandler<br/>ProblemDetail 400"]
    Act["Actuator<br/>/actuator/health<br/>/actuator/info<br/>/actuator/metrics"]
    App --> Ctrl
    Ctrl --> Svc
    Ctrl -.-> Err
    App --> Act
  end

  Client["HTTP client"] -->|"JSON / problem+json"| Ctrl
  Client -->|"JSON"| Act

  subgraph image["Implemented: container image"]
    Docker["Dockerfile<br/>multi-stage Maven build<br/>Temurin JRE, USER app"]
  end

  Docker -.->|"packages JAR"| App

  subgraph ci["Implemented: GitHub Actions"]
    GHA["ci.yml<br/>reusable java-maven-ci@v0.2.0<br/>docker build (no push)"]
  end

  GHA -.->|"mvn test"| App
  GHA -.->|"docker build"| Docker

  subgraph aks_target["Documented target — not applied from this repo"]
    AKS["AKS cluster + sample manifests"]
  end

  Docker -.->|"operator push + apply"| aks_target
```

## How to read it

| Piece | In tree? |
| --- | --- |
| `HelloController` / `HelloService` | Yes |
| `ApiExceptionHandler` (Problem Details) | Yes |
| Actuator `health` / `info` / `metrics` only | Yes |
| Multi-stage non-root Dockerfile | Yes |
| CI (`mvn test` + docker build, no push) | Yes |
| `deploy/k8s/` sample manifests | Yes — reference only |
| Live AKS apply / Ingress / registry push | **No** — not claimed |
| springdoc-openapi | Deferred — see api-errors.md |

When the runtime shape changes, update this file in the same PR.

## Ops notes (v1.1+)

- Dockerfile/Compose HEALTHCHECK and k8s probes share Actuator liveness/readiness paths.
- Graceful shutdown + `terminationGracePeriodSeconds` documented under operations.
- Config vs Secret patterns: [config-secrets.md](../deployment/config-secrets.md).
