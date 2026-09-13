# Architecture diagram

Mermaid view of components **that exist in the tree**. AKS is drawn as an aspirational boundary, not a live deploy.

```mermaid
flowchart TB
  subgraph local_or_container["Implemented: process / container"]
    App["SpringBootAksBlueprintApplication"]
    Ctrl["HelloController<br/>GET /api/v1/hello"]
    Svc["HelloService"]
    Act["Actuator<br/>health / info / metrics"]
    App --> Ctrl
    Ctrl --> Svc
    App --> Act
  end

  Client["HTTP client"] --> Ctrl
  Client --> Act

  subgraph image["Implemented: container image"]
    Docker["Dockerfile<br/>multi-stage, non-root"]
  end

  Docker -.->|"packages"| App

  subgraph aks_target["Documented target — not applied from this repo"]
    AKS["AKS cluster"]
  end

  image -.->|"operator push + apply later"| aks_target
```

## How to read it

- **Implemented:** Spring Boot app, carefully exposed Actuator endpoints, unit/WebMvc tests, Dockerfile.
- **Not in this slice:** GitHub Actions, Kubernetes manifests, Ingress, managed databases.
- When CI or `deploy/k8s/` land, update this diagram in the same PR.
