# Spring Boot AKS Blueprint

[![CI](https://github.com/rmkr-dev/spring-boot-aks-blueprint/actions/workflows/ci.yml/badge.svg)](https://github.com/rmkr-dev/spring-boot-aks-blueprint/actions/workflows/ci.yml)

A personal engineering blueprint for a **Java 21 / Spring Boot 3** HTTP service aimed at **Azure Kubernetes Service (AKS)**. Honest docs, a small production-shaped app, container build, GitHub Actions CI, and **reference-only** Kubernetes samples—not a claim that anything is running in a live cluster.

## Why this exists

Greenfield Spring Boot + Kubernetes repos often ship either an empty README or a wall of aspirational YAML. This blueprint keeps each layer reviewable:

1. **Docs and guardrails** — what the system is, how to change it, what “done” means
2. **Application + tests** — a minimal REST service you can run and test locally
3. **Observability + Docker** — Actuator exposure, multi-stage non-root image
4. **CI + hygiene** — GitHub Actions (`mvn test` + docker build without push), Dependabot, templates
5. **Architecture + k8s samples** — diagrams, ADR, reference manifests *(this slice)*

## Target stack

| Concern | Choice |
| --- | --- |
| Language / runtime | Java 21 (Temurin) |
| Framework | Spring Boot 3.5.x (Web + Actuator + Validation) |
| Build | Maven |
| Container | Multi-stage Dockerfile, non-root user |
| Orchestration target | AKS (sample manifests are **reference only**) |
| CI | GitHub Actions calling `rmkr-dev/gha-reusable-workflows` `@v0.1.0` |

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

| Variable | Default |
| --- | --- |
| `SERVER_PORT` | `8080` |
| `SPRING_APPLICATION_NAME` | `spring-boot-aks-blueprint` |

### Docker

```bash
docker build -t spring-boot-aks-blueprint:local .
docker run --rm -p 8080:8080 spring-boot-aks-blueprint:local
```

## Continuous integration

Pushes to `main` and pull requests run [`.github/workflows/ci.yml`](.github/workflows/ci.yml):

1. **test** — reusable Java Maven workflow (`mvn -B test`, JDK 21)
2. **docker** — `docker build` on the runner with **no registry push**

Details: [docs/development/ci.md](docs/development/ci.md).

## Repository structure

```text
.
├── AGENTS.md / CONTRIBUTING.md / SECURITY.md / LICENSE
├── README.md
├── pom.xml / Dockerfile / .dockerignore
├── .github/                 # CI, Dependabot, CODEOWNERS, templates
├── src/                     # Spring Boot app + tests
├── deploy/k8s/              # Reference Deployment/Service/ConfigMap
└── docs/
    ├── architecture/        # Diagrams matching the tree
    ├── decisions/           # ADRs (see ADR-001)
    ├── development/
    ├── deployment/
    └── security/
```

## Documentation

| Doc | Purpose |
| --- | --- |
| [AGENTS.md](AGENTS.md) | Guardrails |
| [Development](docs/development/development.md) | Contributor workflow |
| [CI](docs/development/ci.md) | How Actions works |
| [Architecture](docs/architecture/README.md) | System shape + diagrams |
| [ADR-001](docs/decisions/0001-spring-boot-aks-blueprint.md) | Blueprint decision |
| [Deployment](docs/deployment/deployment.md) | Local, container, reference k8s |
| [Security](docs/security/security.md) | Security posture |
| [SECURITY.md](SECURITY.md) | Vulnerability reporting |

## What is not claimed

- No applied AKS cluster, Ingress, or live public URL from this repo
- Sample manifests under `deploy/k8s/` are **reference only**
- CI builds the image but does **not** push it or run `kubectl apply`

## Contributing

Read [CONTRIBUTING.md](CONTRIBUTING.md) and [AGENTS.md](AGENTS.md). Prefer one complete slice per PR. `mvn -B test` must pass when application code changes; wait for CI green.

## License

[MIT](LICENSE)
