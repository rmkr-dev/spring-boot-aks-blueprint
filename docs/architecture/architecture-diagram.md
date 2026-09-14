# Architecture diagram

Mermaid view of components **that exist in the tree**. AKS appears only as a documented target.

```mermaid
flowchart TB
  subgraph jvm["Implemented: Spring Boot process"]
    App["SpringBootAksBlueprintApplication"]
    Ctrl["HelloController<br/>GET /api/v1/hello"]
    Svc["HelloService"]
    Err["ApiExceptionHandler<br/>ProblemDetail 400"]
    Headers["SecurityHeadersFilter"]
    ReqId["RequestIdFilter<br/>X-Request-Id + MDC"]
    Act["Actuator<br/>health / info / metrics / prometheus"]
    App --> Ctrl
    Ctrl --> Svc
    Ctrl -.-> Err
    App --> Headers
    App --> ReqId
    App --> Act
  end

  Client["HTTP client"] -->|"JSON / problem+json"| Ctrl
  Client -->|"text / JSON"| Act

  subgraph image["Implemented: container image"]
    Docker["Dockerfile<br/>multi-stage Maven build<br/>Temurin JRE, USER app<br/>HEALTHCHECK liveness"]
  end

  Docker -.->|"packages JAR"| App

  subgraph ci["Implemented: GitHub Actions"]
    GHA["ci.yml<br/>reusable java-maven-ci@v0.4.0<br/>docker build (no push)<br/>concurrency cancel-in-progress"]
  end

  GHA -.->|"mvn test"| App
  GHA -.->|"docker build"| Docker

  subgraph aks_target["Documented target — not applied from this repo"]
    AKS["AKS + deploy/k8s samples<br/>SA / PDB / HPA / NP / Ingress<br/>emptyDir /tmp + scrape annotations"]
  end

  Docker -.->|"operator push + apply"| aks_target
```

## How to read it

| Piece | In tree? |
| --- | --- |
| `HelloController` / `HelloService` | Yes |
| `ApiExceptionHandler` (Problem Details) | Yes |
| `SecurityHeadersFilter` / `RequestIdFilter` | Yes |
| Actuator `health` / `info` / `metrics` / `prometheus` | Yes |
| Virtual threads + Tomcat timeouts + compression + forward-headers | Yes |
| Multi-stage non-root Dockerfile + Compose healthcheck | Yes |
| CI (`mvn test` + docker build, no push) | Yes |
| `deploy/k8s/` sample manifests | Yes — reference only |
| Live AKS apply / registry push | **No** — not claimed |
| springdoc-openapi | Deferred — see api-errors.md |

When the runtime shape changes, update this file in the same PR.

## Ops notes (v1.1+ / Unreleased)

- Dockerfile/Compose HEALTHCHECK and k8s probes share Actuator liveness/readiness paths.
- Graceful shutdown + `terminationGracePeriodSeconds` documented under operations.
- Config vs Secret patterns: [config-secrets.md](../deployment/config-secrets.md).
- Prometheus scrape path + optional pod annotations; request-id logging via `logback-spring.xml`.
