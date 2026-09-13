# Architecture

## Current state

This repository is a **Spring Boot + AKS engineering blueprint**. The application and container slices add a runnable Java 21 / Spring Boot 3 service with tests and a multi-stage non-root image. CI workflows and Kubernetes manifests are still planned.

| Surface | Role | Status |
| --- | --- | --- |
| `README.md` / `AGENTS.md` / `CONTRIBUTING.md` | Entry and guardrails | Present |
| `docs/*` | Architecture, security, development, deployment | Present |
| `pom.xml` | Maven / Spring Boot 3.5 / Java 21 | Present |
| `src/main/java/dev/rmkr/blueprint` | App, `/api/v1/hello`, Actuator config | Present |
| `src/test/java/...` | Unit + WebMvc tests (`mvn test`) | Present |
| `Dockerfile` / `.dockerignore` | Multi-stage non-root image | Present |
| `.github/` | Actions, Dependabot, templates | Planned — CI slice |
| `deploy/k8s/` | Sample manifests | Planned — k8s slice |

## Application components

```text
SpringBootAksBlueprintApplication
  └── HelloController (/api/v1/hello)
        └── HelloService  (message + spring.application.name)
Actuator (exposed): /actuator/health, /actuator/info, /actuator/metrics
```

Configuration prefers environment variables over hardcoding (`SERVER_PORT`, `SPRING_APPLICATION_NAME`). Health probes are enabled for future Kubernetes use; health details are not shown anonymously. There is no authentication layer and no datastore in this blueprint.

## Network posture

Local and container processes listen on port 8080. The AKS path (Internet → LB/Ingress → Service → Pod) remains a **documented target**; see [network-diagram.md](network-diagram.md). No cluster apply is claimed.

## Related decisions

ADRs for Spring Boot + AKS choices land with the architecture slice. Link them here when added.

## What is intentionally out of scope here

- Claiming a production AKS deployment
- Auth, databases, or multi-service meshes
- Live CI badges before workflows exist
