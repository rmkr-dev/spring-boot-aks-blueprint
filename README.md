# Spring Boot AKS Blueprint

[![CI](https://github.com/rmkr-dev/spring-boot-aks-blueprint/actions/workflows/ci.yml/badge.svg)](https://github.com/rmkr-dev/spring-boot-aks-blueprint/actions/workflows/ci.yml)

A personal engineering blueprint for a **Java 21 / Spring Boot 3** HTTP service aimed at **Azure Kubernetes Service (AKS)**. Honest docs, a small production-shaped app, container build, and GitHub Actions CI—not a claim that anything is running in a live cluster.

## Why this exists

Greenfield Spring Boot + Kubernetes repos often ship either an empty README or a wall of aspirational YAML. This blueprint keeps each layer reviewable:

1. **Docs and guardrails** — what the system is, how to change it, what “done” means
2. **Application + tests** — a minimal REST service you can run and test locally
3. **Observability + Docker** — Actuator exposure, multi-stage non-root image
4. **CI + hygiene** — GitHub Actions (`mvn test` + docker build without push), Dependabot, templates *(this slice)*
5. **Architecture + k8s samples** — diagrams, ADR, reference manifests

## Target stack

| Concern | Choice |
| --- | --- |
| Language / runtime | Java 21 (Temurin) |
| Framework | Spring Boot 3.5.x (Web + Actuator + Validation) |
| Build | Maven |
| Container | Multi-stage Dockerfile, non-root user |
| Orchestration target | AKS (documented path; manifests arrive in a later slice) |
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

## Continuous integration

Pushes to `main` and pull requests run [`.github/workflows/ci.yml`](.github/workflows/ci.yml):

1. **test** — reusable Java Maven workflow (`mvn -B test`, JDK 21)
2. **docker** — `docker build` on the runner with **no registry push**

Details: [docs/development/ci.md](docs/development/ci.md).

## Repository structure

```text
.
├── AGENTS.md
├── CONTRIBUTING.md
├── LICENSE
├── README.md
├── SECURITY.md
├── pom.xml
├── Dockerfile
├── .dockerignore
├── .github/                        # CI, Dependabot, CODEOWNERS, templates
├── src/main/java/dev/rmkr/blueprint/
├── src/main/resources/application.yml
├── src/test/java/...
└── docs/
    ├── architecture/
    ├── decisions/
    ├── development/
    ├── deployment/
    └── security/
```

`deploy/k8s/` lands in the architecture/k8s slice.

## Documentation

| Doc | Purpose |
| --- | --- |
| [AGENTS.md](AGENTS.md) | Guardrails for humans and coding agents |
| [CONTRIBUTING.md](CONTRIBUTING.md) | How to open a change |
| [Development](docs/development/development.md) | Contributor workflow |
| [CI](docs/development/ci.md) | How GitHub Actions works |
| [Architecture](docs/architecture/README.md) | System shape |
| [Security](docs/security/security.md) | Security posture |
| [SECURITY.md](SECURITY.md) | Vulnerability reporting |
| [Deployment](docs/deployment/deployment.md) | Local, container, AKS reference path |

## What is not claimed yet

- No applied AKS cluster, Ingress, or live URL
- No Kubernetes manifests yet (next slice)
- CI builds the image but does **not** push it

## Contributing

Read [CONTRIBUTING.md](CONTRIBUTING.md) and [AGENTS.md](AGENTS.md). Prefer one complete slice per PR. `mvn -B test` must pass when application code changes; wait for CI green when workflows exist.

## License

[MIT](LICENSE)
