# Spring Boot AKS Blueprint

[![CI](https://github.com/rmkr-dev/spring-boot-aks-blueprint/actions/workflows/ci.yml/badge.svg)](https://github.com/rmkr-dev/spring-boot-aks-blueprint/actions/workflows/ci.yml)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)
[![Java](https://img.shields.io/badge/Java-21-orange.svg)](pom.xml)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.x-green.svg)](pom.xml)

A personal **v1.3.0** engineering blueprint for a **Java 21 / Spring Boot 3.5** HTTP service aimed at **Azure Kubernetes Service (AKS)**. Honest docs, a runnable app with tests, multi-stage non-root container, GitHub Actions CI pinned to reusable workflows, and **reference-only** Kubernetes samples—not a claim that anything is running in a live cluster.

## Why this exists

Greenfield Spring Boot + Kubernetes repos often ship either an empty README or a wall of aspirational YAML. This blueprint keeps each layer reviewable and present on `main`:

1. Docs and guardrails (`AGENTS.md`, CONTRIBUTING, SECURITY)
2. Application + tests (`GET /api/v1/hello`, Problem Details errors)
3. Observability + Docker (Actuator + Prometheus, request-id, custom metric/info, multi-stage image)
4. CI + hygiene (reusable Maven CI `@v0.4.0`, Dependabot, templates)
5. Architecture + ADR + reference `deploy/k8s/` manifests

## Target stack

| Concern | Choice |
| --- | --- |
| Language / runtime | Java 21 (Temurin) |
| Framework | Spring Boot **3.5.x** (Web + Actuator + Validation, virtual threads) — not Boot 4 |
| Build | Maven |
| Container | Multi-stage Dockerfile, non-root `app` user |
| Orchestration | AKS path documented; manifests under `deploy/k8s/` are **reference only** |
| CI | `rmkr-dev/gha-reusable-workflows/.github/workflows/java-maven-ci.yml@v0.4.0` + local docker build (no push) |

No Node/npm. No fake auth. No secrets in the tree. **springdoc-openapi deferred** until the public API grows (see [api-errors.md](docs/development/api-errors.md)).

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
- Prometheus: `http://localhost:8080/actuator/prometheus`
- Info: `http://localhost:8080/actuator/info`

| Variable | Default |
| --- | --- |
| `SERVER_PORT` | `8080` |
| `SPRING_APPLICATION_NAME` | `spring-boot-aks-blueprint` |

### Docker

The image includes a `HEALTHCHECK` against `/actuator/health/liveness` (see [docker-healthcheck.md](docs/deployment/docker-healthcheck.md)).

```bash
docker build -t spring-boot-aks-blueprint:local .
docker run --rm -p 8080:8080 spring-boot-aks-blueprint:local
```

Or Compose (app image only, no database):

```bash
docker compose up --build
```

### Optional load smoke

With the app listening on 8080:

```bash
./scripts/load-hello.sh http://127.0.0.1:8080 50
python3 scripts/load-hello.py http://127.0.0.1:8080 50
```

See [load-hello.md](docs/development/load-hello.md).

Response headers include conservative security defaults and `X-Request-Id` (see [http-headers.md](docs/security/http-headers.md) and [request-id.md](docs/operations/request-id.md)).

## Continuous integration

Pushes to `main` and pull requests run [`.github/workflows/ci.yml`](.github/workflows/ci.yml):

1. **test** — reusable Java Maven workflow pinned at **`@v0.4.0`** (`mvn -B test`, JDK 21)
2. **docker** — `docker build` on the runner with **no registry push**

Superseded runs on the same ref are cancelled via workflow concurrency.

Details: [docs/development/ci.md](docs/development/ci.md).

## Repository structure

```text
.
├── AGENTS.md / CONTRIBUTING.md / SECURITY.md / LICENSE / CHANGELOG.md
├── README.md
├── pom.xml / Dockerfile / compose.yaml / .dockerignore
├── .github/                 # CI (@v0.4.0 pin), Dependabot, CODEOWNERS, templates
├── src/                     # Spring Boot app + tests
├── scripts/                 # Optional load-hello smokes (bash/Python)
├── deploy/k8s/              # Reference Deployment/Service/ConfigMap/HPA/NP/PDB/SA/Ingress
└── docs/
    ├── architecture/        # Diagrams matching the tree
    ├── decisions/           # ADR-001
    ├── development/         # Workflow, CI, API errors, load-hello
    ├── deployment/          # Local / container / reference k8s
    ├── operations/          # Observability, graceful shutdown, request-id
    └── security/            # HTTP headers + posture
```

## Documentation

| Doc | Purpose |
| --- | --- |
| [AGENTS.md](AGENTS.md) | Guardrails |
| [Development](docs/development/development.md) | Contributor workflow |
| [CI](docs/development/ci.md) | Actions + reusable pin |
| [API errors](docs/development/api-errors.md) | Problem Details + OpenAPI note |
| [Architecture](docs/architecture/README.md) | System shape + diagrams |
| [ADR-001](docs/decisions/0001-spring-boot-aks-blueprint.md) | Blueprint decision |
| [Deployment](docs/deployment/deployment.md) | Local, container, reference k8s |
| [kind notes](docs/deployment/kind.md) | Optional local cluster; Compose preferred for app-only |
| [CHANGELOG](CHANGELOG.md) | Release notes |
| [Observability](docs/operations/observability.md) | Actuator, custom metric, info, prometheus |
| [Graceful shutdown](docs/operations/graceful-shutdown.md) | `server.shutdown=graceful`, preStop budget |
| [Request ID](docs/operations/request-id.md) | `X-Request-Id` + logback MDC |
| [HTTP headers](docs/security/http-headers.md) | Conservative response headers |
| [Security](docs/security/security.md) | Security posture |
| [SECURITY.md](SECURITY.md) | Vulnerability reporting |

## What is not claimed

- No applied AKS cluster, Ingress, or live public URL from this repo
- Sample manifests under `deploy/k8s/` are **reference only**
- CI builds the image but does **not** push it or run `kubectl apply`
- No springdoc/OpenAPI UI in v1

## Contributing

Read [CONTRIBUTING.md](CONTRIBUTING.md) and [AGENTS.md](AGENTS.md). Prefer multi-commit, one-slice PRs. `mvn -B test` must pass; wait for CI green. Dependabot ignores Spring Boot **major** upgrades (stay on 3.5.x).

## License

[MIT](LICENSE)
