# Changelog

All notable changes to this project are documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Added
- HTTP response compression for JSON/problem+json/text (min 1 KiB)
- `server.forward-headers-strategy=framework` for Ingress/`X-Forwarded-*` support
- `RequestIdFilter` for `X-Request-Id` correlation (echo or generate) + MDC
- Explicit Tomcat `connection-timeout` (10s) and `keep-alive-timeout` (20s)
- Operator notes for namespace ResourceQuota/LimitRange (`docs/deployment/namespace-quotas.md`)
- Dependabot Maven groups for Spring Boot minor/patch vs other libraries
- Soft `topologySpreadConstraints` on the Deployment sample (hostname skew)
- Java 21 virtual threads enabled (`spring.threads.virtual.enabled=true`)
- Conservative HTTP security headers via `SecurityHeadersFilter`
- Micrometer Prometheus registry (`/actuator/prometheus`) and optional scrape annotations on the Deployment
- Deployment `emptyDir` volume at `/tmp` so read-only root filesystem pods have JVM scratch space
- Compose `healthcheck` aligned with Dockerfile Actuator liveness
- Unit/integration assertions for `server.shutdown=graceful` and lifecycle timeout
- Architecture inventory refreshed for Compose healthcheck, scripts, secrets example, graceful shutdown
- NetworkPolicy sample notes optional Ingress-controller allow rule (commented)
- CONTRIBUTING/AGENTS pointers for load-hello, graceful shutdown, and Docker healthcheck docs
- kind.md notes for Ingress sample and expanded k8s inventory

## [1.1.0] — 2026-09-14

Minor blueprint refresh after v1.0.0: more reference Kubernetes samples, ops hardening, tests, and local tooling.

### Added
- Reference `deploy/k8s/ingress.yaml` Ingress sample (controller/host; TLS commented)
- Reference `deploy/k8s/pdb.yaml` PodDisruptionBudget (`minAvailable: 1`)
- Reference `deploy/k8s/serviceaccount.yaml` with token automount disabled (Deployment wired)
- ConfigMap/Secret pattern docs (`docs/deployment/config-secrets.md`) and `secret.example.yaml` (no real values)
- Graceful shutdown (`server.shutdown=graceful`, 30s lifecycle timeout) and Deployment `terminationGracePeriodSeconds: 45`
- Dockerfile `HEALTHCHECK` on Actuator liveness (`curl`; start-period 45s)
- Dependabot group for GitHub Actions updates (`github-actions`)
- Optional `scripts/load-hello.sh` / `scripts/load-hello.py` smoke load for `/api/v1/hello` (no Node)
- Expanded MockMvc / Actuator metric tag-filter and HelloService counter coverage
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

[1.1.0]: https://github.com/rmkr-dev/spring-boot-aks-blueprint/releases/tag/v1.1.0
[1.0.0]: https://github.com/rmkr-dev/spring-boot-aks-blueprint/releases/tag/v1.0.0
