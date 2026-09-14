# Kubernetes samples (reference only)

These manifests illustrate a minimal Deployment, Service, ConfigMap, optional HPA, and a sample Ingress for the Spring Boot app.

**They are not applied by CI and are not proof of a live AKS cluster.**

| File | Purpose |
| --- | --- |
| `configmap.yaml` | Non-secret env |
| `deployment.yaml` | Pod template, probes, resources |
| `service.yaml` | ClusterIP |
| `hpa.yaml` | CPU-based HorizontalPodAutoscaler (optional) |
| `networkpolicy.yaml` | Sample ingress/egress NetworkPolicy (optional; CNI-dependent) |
| `pdb.yaml` | Optional PodDisruptionBudget (`minAvailable: 1`) |
| `serviceaccount.yaml` | Dedicated ServiceAccount; token automount disabled |
| `ingress.yaml` | Optional Ingress sample (controller + host/TLS required) |
| `secret.example.yaml` | Secret **shape** only (no real values; copy out of band) |

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

`networkpolicy.yaml` is an optional sample that selects pods labeled `app.kubernetes.io/name=spring-boot-aks-blueprint`. It allows ingress on TCP 8080 from same-namespace pods and from pods labeled `app.kubernetes.io/component=api-client`, plus limited egress (DNS and HTTPS). **Enforcement depends on your CNI.** Treat it as a starting point—tighten for real clusters. If you use `ingress.yaml`, uncomment the optional Ingress-controller ingress rule (or equivalent) so the controller can reach pods. Not applied by CI.

## PodDisruptionBudget

`pdb.yaml` requests `minAvailable: 1` for the app pods. Pair with HPA (`maxReplicas > 1`) so voluntary disruptions can leave capacity. Reference only.

## ServiceAccount

`serviceaccount.yaml` defines a dedicated account with `automountServiceAccountToken: false`. The Deployment sets `serviceAccountName` and also disables token automount on the pod. No RBAC Roles are shipped—add them only when the app needs API access.


## Ingress

`ingress.yaml` is an optional sample that routes host `spring-boot-aks-blueprint.example.local` to the ClusterIP Service on port 80. It sets `ingressClassName: nginx` as a common placeholder—swap for AGIC or your controller. TLS is commented out; create a Secret out of band and uncomment when ready. **Requires an Ingress controller in the cluster.** Not applied by CI; no live public URL is claimed.


## Config and secrets

Non-secret env lives in `configmap.yaml`. Credentials use Kubernetes Secrets created **out of band**—see `secret.example.yaml` (placeholders only) and [config-secrets.md](../../docs/deployment/config-secrets.md). External Secrets / Key Vault sync is a documented future option, not shipped as a CRD here. Never commit live `stringData` or `data` payloads.






## Namespace quotas

`ResourceQuota` / `LimitRange` are **not** shipped here—they are namespace/operator concerns. See [namespace-quotas.md](../../docs/deployment/namespace-quotas.md) for a checklist when placing this Deployment in a constrained AKS namespace.

## Topology spread

The Deployment sample includes soft `topologySpreadConstraints` (`whenUnsatisfiable: ScheduleAnyway`, `topologyKey: kubernetes.io/hostname`, `maxSkew: 1`) so schedulers prefer spreading replicas across nodes when capacity allows. Pair with HPA (`maxReplicas > 1`) for meaningful effect. Reference only—cluster topology and taints still win.

## Prometheus scrape annotations

The Deployment pod template includes optional `prometheus.io/scrape`, `prometheus.io/path`, and `prometheus.io/port` annotations pointing at `/actuator/prometheus`. They help operators who already run a Prometheus that honors those annotations. **No scrape, ServiceMonitor, or live metrics pipeline is claimed by this repo.**

## Read-only root filesystem and `/tmp`

The Deployment sets `readOnlyRootFilesystem: true` on the container. The JVM and many libraries expect a writable temp directory, so the sample mounts an `emptyDir` volume at `/tmp`. Without that mount, the process can fail at runtime even though local `java -jar` works on a writable disk. Tune `emptyDir` size limits in real clusters if needed.

## Rollout readiness

The Deployment sets `minReadySeconds: 10` so a newly Ready pod must stay Ready briefly before the rolling update proceeds, and `progressDeadlineSeconds: 120` so a stalled rollout surfaces as a ProgressDeadlineExceeded condition sooner than the 600s default. Pair with readiness probes; tune for your JVM warm-up. Reference only.

## Graceful shutdown

The Deployment sets `terminationGracePeriodSeconds: 45` and a container `preStop` exec of `sleep 5`. Pair with `server.shutdown=graceful` and `spring.lifecycle.timeout-per-shutdown-phase=30s` in `application.yml` so EndpointSlice removal can start before SIGTERM, then in-flight requests can finish (5 + 30 + buffer ≤ 45). Details: [graceful-shutdown.md](../../docs/operations/graceful-shutdown.md).

Suggested local review (optional):

```bash
kubectl apply --dry-run=client -f deploy/k8s/
```

Replace the container `image` with one you built and pushed. Do not commit secrets; follow [config-secrets.md](../../docs/deployment/config-secrets.md) and keep credentials out of git.
