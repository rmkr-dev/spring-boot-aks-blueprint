# Architecture diagram

Mermaid view of components **that exist in the tree**. AKS appears only as a documented target.

```mermaid
flowchart TB
  subgraph jvm["Implemented: Spring Boot process"]
    App["SpringBootAksBlueprintApplication"]
    Ctrl["HelloController<br/>GET /api/v1/hello"]
    Svc["HelloService"]
    Act["Actuator<br/>/actuator/health<br/>/actuator/info<br/>/actuator/metrics"]
    App --> Ctrl
    Ctrl --> Svc
    App --> Act
  end

  Client["HTTP client"] -->|"JSON"| Ctrl
  Client -->|"JSON"| Act

  subgraph image["Implemented: container image"]
    Docker["Dockerfile<br/>multi-stage Maven build<br/>Temurin JRE, USER app"]
  end

  Docker -.->|"packages JAR"| App

  subgraph ci["Implemented: GitHub Actions"]
    GHA["ci.yml<br/>reusable java-maven-ci@v0.1.0<br/>docker build (no push)"]
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
| Actuator `health` / `info` / `metrics` only | Yes |
| Multi-stage non-root Dockerfile | Yes |
| CI (`mvn test` + docker build, no push) | Yes |
| `deploy/k8s/` sample manifests | Same PR as this diagram when landed |
| Live AKS apply / Ingress / registry push | **No** — not claimed |

When the runtime shape changes, update this file in the same PR.
