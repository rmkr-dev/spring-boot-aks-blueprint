# ADR-001: Spring Boot + AKS engineering blueprint

## Status

Accepted

## Context

Personal projects often need a small, honest starting point for a JVM HTTP service aimed at Azure Kubernetes Service. Empty READMEs and speculative YAML both waste review time. This repository should stay cloneable, testable locally, and clear about what is implemented versus aspirational.

## Decision

1. **Java 21 + Spring Boot 3.x (Maven)** for the sample service (`web`, `actuator`, `validation`).
2. **Config over hardcoding** via `application.yml` and environment variables (`SERVER_PORT`, `SPRING_APPLICATION_NAME`).
3. **Careful Actuator exposure**: only `health`, `info`, and `metrics` on the web endpoint set; health details restricted; probes enabled for future k8s use.
4. **Multi-stage non-root Dockerfile**; CI may build the image but does not push to a registry by default.
5. **GitHub Actions** calling pinned `rmkr-dev/gha-reusable-workflows` `java-maven-ci.yml` at an annotated tag (**currently `@v0.2.0`**) for `mvn -B test`.
6. **Reference-only Kubernetes samples** under `deploy/k8s/` with deployment docs that never claim a live apply from this repo.
7. **No Node/npm**, no secrets in tree, no company names, no speculative auth/database stacks.

## Consequences

- Contributors can run `mvn -B test` and (optionally) Docker without Azure credentials.
- Diagrams and ADRs must be updated in the same PR when the system shape changes.
- Operators who deploy to AKS own registry credentials, Ingress, TLS, and cluster access outside git.
- Bump the reusable workflow pin deliberately (annotated tags only); document the pin in `docs/development/ci.md`.

## Alternatives considered

- **Spring Boot without Actuator** — rejected; health/metrics are baseline for containers.
- **Helm chart as the first deliverable** — deferred; plain manifests are easier to review for a blueprint.
- **Floating `@main` reusable workflows** — rejected for consumers; pin annotated tags.
