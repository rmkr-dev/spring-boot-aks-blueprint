# Architecture

## Current state

This repository is a **Spring Boot + AKS engineering blueprint**. The application slice adds a runnable Java 21 / Spring Boot 3 service with tests. Container image, CI, and Kubernetes manifests are still planned.

| Surface | Role | Status |
| --- | --- | --- |
| `README.md` / `AGENTS.md` / `CONTRIBUTING.md` | Entry and guardrails | Present |
| `docs/*` | Architecture, security, development, deployment | Present |
| `pom.xml` | Maven / Spring Boot 3.5 / Java 21 | Present |
| `src/main/java/dev/rmkr/blueprint` | App, `/api/v1/hello` | Present |
| `src/test/java/...` | Unit + WebMvc tests (`mvn test`) | Present |
| `Dockerfile` | Multi-stage non-root image | Planned — observability/Docker slice |
| `.github/` | Actions, Dependabot, templates | Planned — CI slice |
| `deploy/k8s/` | Sample manifests | Planned — k8s slice |

## Application components

```text
SpringBootAksBlueprintApplication
  └── HelloController (/api/v1/hello)
        └── HelloService  (message + spring.application.name)
Actuator dependency present; careful endpoint exposure lands with the Docker slice
```

Configuration prefers environment variables over hardcoding (`SERVER_PORT`, `SPRING_APPLICATION_NAME`). There is no authentication layer and no datastore in this blueprint.

## Network posture

The local process listens on port 8080. The AKS path remains a **documented target**; see [network-diagram.md](network-diagram.md). No cluster apply is claimed.

## Related decisions

ADRs for Spring Boot + AKS choices land with the architecture slice. Link them here when added.

## What is intentionally out of scope here

- Claiming a production AKS deployment
- Auth, databases, or multi-service meshes
- Live CI badges before workflows exist
- Dockerfile / container run (next slice)
