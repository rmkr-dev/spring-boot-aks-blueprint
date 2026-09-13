# Spring Boot AKS Blueprint

A personal engineering blueprint for a **Java 21 / Spring Boot 3** HTTP service aimed at **Azure Kubernetes Service (AKS)**. It is a starting point for portfolio and side projects: honest docs, a small production-shaped app, container build, and reference Kubernetes manifests—not a claim that anything is running in a live cluster.

## Why this exists

Greenfield Spring Boot + Kubernetes repos often ship either an empty README or a wall of aspirational YAML. This blueprint keeps each layer reviewable:

1. **Docs and guardrails** — what the system is, how to change it, what “done” means
2. **Application + tests** — a minimal REST service you can run and test locally
3. **CI + diagrams + ADRs** — GitHub Actions that actually run `mvn test`, architecture that matches the tree
4. **Reference k8s manifests** — sample Deployment/Service/ConfigMap with clear “not applied” language

Use it as a clone-and-adapt base. Replace sample packages and paths when you build a real product.

## Target stack

| Concern | Choice |
| --- | --- |
| Language / runtime | Java 21 (Temurin) |
| Framework | Spring Boot 3.x (Web + Actuator) |
| Build | Maven |
| Container | Multi-stage Dockerfile, non-root user |
| Orchestration target | AKS (documented path; manifests are reference-only until you apply them) |
| CI | GitHub Actions (local workflow mirroring the intended reusable-workflow contract) |

No Node/npm. No fake auth. No secrets in the tree.

## How to run locally

Prerequisites: **JDK 21** and **Maven 3.9+**.

```bash
# From the repository root (after the application slice lands)
mvn -B test
mvn -B spring-boot:run
```

Then open:

- Sample API: `http://localhost:8080/api/v1/hello`
- Health: `http://localhost:8080/actuator/health`

Configuration is env-overridable (see `application.yml` when present): `SERVER_PORT`, `SPRING_APPLICATION_NAME`.

Docker (after the application slice):

```bash
docker build -t spring-boot-aks-blueprint:local .
docker run --rm -p 8080:8080 spring-boot-aks-blueprint:local
```

## Repository structure

```text
.
├── AGENTS.md                 # Read before changing code or docs
├── CONTRIBUTING.md
├── LICENSE
├── README.md
├── docs/
│   ├── architecture/         # Current system + diagrams (filled in later slices)
│   ├── decisions/            # ADRs
│   ├── development/          # Contributor workflow
│   ├── deployment/           # AKS path (honest: reference until applied)
│   └── security/
├── src/                      # Spring Boot app (application slice)
├── deploy/k8s/               # Sample manifests (k8s slice)
└── .github/                  # CI, Dependabot, templates (CI slice)
```

Early slices intentionally omit later directories. Do not assume `src/` or `.github/` exist until those PRs merge.

## Documentation

| Doc | Purpose |
| --- | --- |
| [AGENTS.md](AGENTS.md) | Guardrails for humans and coding agents |
| [CONTRIBUTING.md](CONTRIBUTING.md) | How to open a change |
| [Development](docs/development/development.md) | Day-to-day workflow |
| [Architecture stubs](docs/architecture/README.md) | How architecture docs are organized |
| [Security](docs/security/security.md) | Security posture |
| [Deployment](docs/deployment/deployment.md) | Local, container, and AKS reference path |

## What is not claimed yet

Until later slices land and this README is updated:

- No Spring Boot source in this foundation slice
- No GitHub Actions workflows
- No applied AKS cluster, Ingress, or live URL

## Contributing

Read [CONTRIBUTING.md](CONTRIBUTING.md) and [AGENTS.md](AGENTS.md). Prefer one complete slice per PR.

## License

[MIT](LICENSE)
