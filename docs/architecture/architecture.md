# Architecture

## Current state

This repository is a **Spring Boot + AKS engineering blueprint**. At this foundation slice it contains documentation and process guardrails only. Application source, CI, diagrams with real components, and Kubernetes manifests arrive in later slices.

| Surface | Role | Status in this slice |
| --- | --- | --- |
| `README.md` | Entry point, local run (when app exists), structure | Present |
| `AGENTS.md` | Human and coding-agent guardrails | Present |
| `CONTRIBUTING.md` | How to propose changes | Present |
| `docs/architecture/` | Current-state narrative and diagram stubs | Present (stubs) |
| `docs/decisions/` | Architecture Decision Records | Index present; ADRs in later slices |
| `docs/development/` | Contributor workflow | Present |
| `docs/deployment/` | Local / container / AKS reference path | Present |
| `docs/security/` | Security posture | Present |
| `src/` Spring Boot app | HTTP API + Actuator | Planned — application slice |
| `.github/` | Actions, Dependabot, templates | Planned — CI slice |
| `deploy/k8s/` | Sample manifests | Planned — k8s slice |

## Intended shape (after later slices)

When complete, the blueprint is:

1. A Maven Spring Boot 3 service on Java 21 with a sample REST endpoint and Actuator health/metrics.
2. A multi-stage non-root Dockerfile.
3. GitHub Actions CI that runs `mvn -B test` (and optionally builds the image without pushing).
4. Reference Kubernetes manifests for Deployment, Service, and ConfigMap aimed at AKS—**not** evidence of a live apply.

Until those files exist, do not treat the intended shape as current state.

## Network posture

See [network-diagram.md](network-diagram.md). Until the application and manifests exist, there is no runtime network in this tree. The AKS path (Internet → LB/Ingress → Service → Pod) is documented as a **target**, not a deployed topology.

## Related decisions

ADRs will land with the CI/architecture slice (for example Spring Boot + AKS blueprint choices). Link them here when added.

## What is intentionally out of scope here

- Claiming a production AKS deployment from this repository
- Auth, databases, or multi-service meshes in the foundation
- Live CI badges before workflows exist
