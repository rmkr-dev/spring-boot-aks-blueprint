# Kubernetes samples (reference only)

These manifests illustrate a minimal Deployment, Service, ConfigMap, and optional HPA for the Spring Boot app.

**They are not applied by CI and are not proof of a live AKS cluster.**

| File | Purpose |
| --- | --- |
| `configmap.yaml` | Non-secret env |
| `deployment.yaml` | Pod template, probes, resources |
| `service.yaml` | ClusterIP |
| `hpa.yaml` | CPU-based HorizontalPodAutoscaler (optional) |
| `networkpolicy.yaml` | Sample ingress/egress NetworkPolicy (optional; CNI-dependent) |

## Probes and resources

The Deployment uses Actuator probe endpoints (requires `management.endpoint.health.probes.enabled=true` in `application.yml`):

| Probe | Path | Role |
| --- | --- | --- |
| `startupProbe` | `/actuator/health/liveness` | Delays liveness until the process is up |
| `readinessProbe` | `/actuator/health/readiness` | Controls Service traffic |
| `livenessProbe` | `/actuator/health/liveness` | Restarts a stuck process |

Resource requests/limits (`100m`/`1` CPU, `256Mi`/`512Mi` memory) are starter values for a small JVM service—tune for your cluster and load. The Dockerfile sets `MaxRAMPercentage=75.0` so the heap stays within the container limit.

## Horizontal Pod Autoscaler

`hpa.yaml` is an optional sample: scale the Deployment between 1 and 3 replicas when average CPU utilization exceeds 70%. It needs a cluster metrics source (for example metrics-server). It does **not** run from this repo's CI.

## NetworkPolicy

`networkpolicy.yaml` is an optional sample that selects pods labeled `app.kubernetes.io/name=spring-boot-aks-blueprint`. It allows ingress on TCP 8080 from same-namespace pods and from pods labeled `app.kubernetes.io/component=api-client`, plus limited egress (DNS and HTTPS). **Enforcement depends on your CNI.** Treat it as a starting point—tighten for real clusters. Not applied by CI.

Suggested local review (optional):

```bash
kubectl apply --dry-run=client -f deploy/k8s/
```

Replace the container `image` with one you built and pushed. Do not commit secrets; use Kubernetes Secrets (out of band) for credentials.
