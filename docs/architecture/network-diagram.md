# Network diagram

Clearly separates **implemented** local/container traffic from the **aspirational AKS** path.

## Implemented today

| Hop | Status |
| --- | --- |
| JVM process on `SERVER_PORT` (default 8080) | Implemented |
| Container exposing 8080 (non-root) | Implemented |
| GitHub Actions docker build (no push) | Implemented |
| Kubernetes Service / Pod (sample YAML) | Reference manifests only — not applied |
| Azure LB / Ingress / TLS / DNS | Sample `ingress.yaml` is reference only; live endpoint is operator-owned |
| Database | Not in this blueprint |

```mermaid
flowchart LR
  Client["HTTP client"] --> Local["localhost:8080<br/>or container :8080"]
  Local --> App["Spring Boot<br/>/api/v1/* + /actuator/health|info|metrics"]
```

## AKS target (aspirational — labeled)

Sample manifests under `deploy/k8s/` describe ClusterIP Service → Pod. They are **reference**. Applying them, attaching an Ingress, and publishing a public URL are operator steps outside this repository.

```mermaid
flowchart LR
  Internet["Internet / clients"] -->|"aspirational"| Ing["Ingress<br/>deploy/k8s/ingress.yaml<br/>(reference; needs controller)"]
  Ing -->|"aspirational"| Svc["Service ClusterIP<br/>deploy/k8s/service.yaml<br/>(reference)"]
  Svc -->|"aspirational"| Pod["Pod: spring-boot-aks-blueprint<br/>deploy/k8s/deployment.yaml<br/>(reference)"]
  Pod --> App["Container :8080<br/>probes → /actuator/health/readiness|liveness"]
  CM["ConfigMap<br/>deploy/k8s/configmap.yaml"] -.->|"non-secret env"| Pod
  NP["NetworkPolicy<br/>deploy/k8s/networkpolicy.yaml<br/>(reference, CNI-dependent)"] -.->|"optional"| Pod
  HPA["HPA<br/>deploy/k8s/hpa.yaml<br/>(reference)"] -.->|"optional scale"| Pod
```

### Legend

- **Implemented:** solid edges in the first diagram; runnable on a laptop or in CI’s docker build.
- **Aspirational / reference:** dashed intent in the second diagram; YAML exists for learning and copy-adapt, not as proof of a live AKS deployment.

Keep secrets out of labels, ConfigMaps, and commit history.
