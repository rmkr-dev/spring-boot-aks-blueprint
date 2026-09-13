# Spring Boot AKS Blueprint

A personal engineering blueprint for a **Java 21 / Spring Boot 3** HTTP service aimed at **Azure Kubernetes Service (AKS)**. Honest docs, a small production-shaped app, and a container build—not a claim that anything is running in a live cluster.

## Why this exists

Greenfield Spring Boot + Kubernetes repos often ship either an empty README or a wall of aspirational YAML. This blueprint keeps each layer reviewable:

1. **Docs and guardrails** — what the system is, how to change it, what “done” means
2. **Application + tests** — a minimal REST service you can run and test locally
3. **Observability + Docker** — Actuator exposure, multi-stage non-root image *(this slice)*
4. **CI + diagrams + ADRs** — GitHub Actions that run `mvn test`, architecture that matches the tree
5. **Reference k8s manifests** — sample Deployment/Service/ConfigMap with clear “not applied” language

## Target stack

| Concern | Choice |
| --- | --- |
| Language / runtime | Java 21 (Temurin) |
| Framework | Spring Boot 3.5.x (Web + Actuator + Validation) |
| Build | Maven |
| Container | Multi-stage Dockerfile, non-root user |
| Orchestration target | AKS (documented path; manifests arrive in a later slice) |
| CI | Planned — workflow calling `rmkr-dev/gha-reusable-workflows` |

No Node/npm. No fake auth. No secrets in the tree.

## How to run locally

Prerequisites: **JDK 21** and **Maven 3.9+**.

```bash
mvn -B test
mvn -B spring-boot:run
```

Then open:

- Sample API: `http://localhost:8080/api/v1/hello` (optional `?name=`)
- Health: `http://localhost:8080/actuator/health`
- Metrics: `http://localhost:8080/actuator/metrics`
- Info: `http://localhost:8080/actuator/info`

Configuration via env (see `src/main/resources/application.yml`):

| Variable | Default |
| --- | --- |
| `SERVER_PORT` | `8080` |
| `SPRING_APPLICATION_NAME` | `spring-boot-aks-blueprint` |

### Docker

```bash
docker build -t spring-boot-aks-blueprint:local .
docker run --rm -p 8080:8080 spring-boot-aks-blueprint:local
```

## Repository structure

```text
.
├── AGENTS.md
├── CONTRIBUTING.md
├── LICENSE
├── README.md
├── pom.xml
├── Dockerfile
├── .dockerignore
├── src/main/java/dev/rmkr/blueprint/   # Application, web, service
├── src/main/resources/application.yml
├── src/test/java/...                   # Unit / WebMvc tests
└── docs/
    ├── architecture/
    ├── decisions/
    ├── development/
    ├── deployment/
    └── security/
```

CI (`.github/`) and `deploy/k8s/` are planned follow-up slices.

## Documentation

| Doc | Purpose |
| --- | --- |
| [AGENTS.md](AGENTS.md) | Guardrails for humans and coding agents |
| [CONTRIBUTING.md](CONTRIBUTING.md) | How to open a change |
| [Development](docs/development/development.md) | Contributor workflow |
| [Architecture](docs/architecture/README.md) | System shape |
| [Security](docs/security/security.md) | Security posture |
| [Deployment](docs/deployment/deployment.md) | Local, container, AKS reference path |

## What is not claimed yet

- No GitHub Actions workflows in this slice
- No applied AKS cluster, Ingress, or live URL
- No Kubernetes manifests yet (next slices)

## Contributing

Read [CONTRIBUTING.md](CONTRIBUTING.md) and [AGENTS.md](AGENTS.md). Prefer one complete slice per PR. `mvn -B test` must pass when application code changes.

## License

[MIT](LICENSE)
