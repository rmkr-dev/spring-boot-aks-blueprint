# Changelog

All notable changes to this project are documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Added
- Reference `deploy/k8s/ingress.yaml` Ingress sample (controller/host; TLS commented)
- Expanded MockMvc edge cases, default/named metric tag filters, and HelloService counter coverage
- ConfigMap/Secret pattern docs (`docs/deployment/config-secrets.md`) and `secret.example.yaml` (no real values)
- Additional MockMvc and Actuator metric tag-filter tests
- Reference `deploy/k8s/pdb.yaml` PodDisruptionBudget (`minAvailable: 1`)
- Reference `deploy/k8s/serviceaccount.yaml` with token automount disabled (Deployment wired)
- CI concurrency group with cancel-in-progress for superseded runs

## [1.0.0] — 2026-09-14

First tagged release of the personal Spring Boot + AKS engineering blueprint.

### Added
- Spring Boot 3.5 / Java 21 sample service with `GET /api/v1/hello` and Problem Details validation errors
- Actuator exposure (`health`, `info`, `metrics`) with Kubernetes readiness/liveness probe support
- Custom Micrometer metrics: `blueprint.hello.requests` (`outcome` tag) and `blueprint.hello.duration` timer
- `BlueprintInfoContributor` for `/actuator/info`
- Multi-stage non-root Dockerfile and single-service `compose.yaml` for local containers
- Reference Kubernetes manifests: Deployment, Service, ConfigMap, HPA, NetworkPolicy
- GitHub Actions CI via `rmkr-dev/gha-reusable-workflows` `java-maven-ci.yml@v0.2.0` plus docker build (no push)
- Architecture diagrams, ADR-001, development/deployment/security/operations docs, kind notes

### Notes
- Manifests and Compose are local/reference convenience—not a live AKS apply
- springdoc-openapi remains deferred until the public API surface justifies it
- Dependabot ignores Spring Boot **major** upgrades (stay on 3.5.x)
- NetworkPolicy enforcement depends on cluster CNI; HPA needs metrics-server

[1.0.0]: https://github.com/rmkr-dev/spring-boot-aks-blueprint/releases/tag/v1.0.0
