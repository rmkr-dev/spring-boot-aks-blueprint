# Network diagram

## Implemented today

| Hop | Status |
| --- | --- |
| JVM process on `SERVER_PORT` (default 8080) | Implemented |
| Container exposing 8080 | Implemented (Dockerfile) |
| Kubernetes Service / Pod | Not in tree yet |
| Azure LB / Ingress / TLS / DNS | Documented target only |
| Database | Optional future — not in this blueprint |

```mermaid
flowchart LR
  Client["HTTP client"] --> Local["localhost:8080<br/>or container :8080"]
  Local --> App["Spring Boot<br/>/api/v1/* + /actuator/*"]
```

## Documented AKS target (not implemented here)

```mermaid
flowchart LR
  Internet["Internet / clients"] --> LB["Azure LB / Ingress<br/>(target)"]
  LB --> Svc["Kubernetes Service<br/>(future reference manifest)"]
  Svc --> Pod["Pod: Spring Boot app"]
  Pod -.->|"optional future"| DB["Managed DB<br/>(not in this blueprint)"]
```

Update this file when manifests or real network boundaries are added. Keep secrets out of labels.
