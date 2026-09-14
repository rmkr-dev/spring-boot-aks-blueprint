# Deployment

Honest deployment path for this blueprint. **Nothing here claims that a production (or any) AKS cluster was applied from this repository.**

## Local process

```bash
mvn -B test
mvn -B spring-boot:run
# or
mvn -B package && java -jar target/spring-boot-aks-blueprint-1.3.2.jar
```

Default listen port: `8080` (override with `SERVER_PORT`).

Useful endpoints:

- API: `http://localhost:8080/api/v1/hello`
- Health: `http://localhost:8080/actuator/health`
- Readiness (K8s): `http://localhost:8080/actuator/health/readiness`
- Liveness (K8s): `http://localhost:8080/actuator/health/liveness`
- Metrics: `http://localhost:8080/actuator/metrics`
- Info: `http://localhost:8080/actuator/info`
- Prometheus: `http://localhost:8080/actuator/prometheus`

## Container image

The image defines a Docker `HEALTHCHECK` against `/actuator/health/liveness` (see [docker-healthcheck.md](docker-healthcheck.md)).

```bash
docker build -t spring-boot-aks-blueprint:local .
docker run --rm -p 8080:8080 spring-boot-aks-blueprint:local
```

Or with Compose (app only — no database service):

```bash
docker compose up --build
```

This uses root [`compose.yaml`](../../compose.yaml) to build the Dockerfile and publish port `8080` (Compose `user: "10001:10001"` matches the image/Deployment identity). Stop with `Ctrl+C` or `docker compose down`. Compose does **not** create a Kubernetes cluster; for cluster experiments use your own kind/minikube and the reference manifests under `deploy/k8s/` (still not applied by this repo's CI).

CI builds the same image on pull requests and `main` pushes **without pushing** to a registry. See [ci.md](../development/ci.md).

## Reference Kubernetes manifests

Samples live under [`deploy/k8s/`](../../deploy/k8s/):

| File | Purpose |
| --- | --- |
| `configmap.yaml` | Non-secret env (`SPRING_APPLICATION_NAME`, `SERVER_PORT`) |
| `deployment.yaml` | Single replica, non-root uid/gid 10001, Actuator probes, CPU/memory requests/limits |
| `service.yaml` | ClusterIP Service on port 80 → container 8080 (`appProtocol: http`) |
| `hpa.yaml` | Optional CPU HPA (1–3 @ 70%) with scale-down stabilization — needs metrics-server |
| `networkpolicy.yaml` | Optional NetworkPolicy sample — CNI must enforce policies |
| `pdb.yaml` | Optional PodDisruptionBudget (`minAvailable: 1`) |
| `serviceaccount.yaml` | Dedicated ServiceAccount (token automount off) |
| `ingress.yaml` | Optional Ingress sample (controller + host; TLS commented) |

Probe paths match Actuator when `management.endpoint.health.probes.enabled=true`:

| Probe | Path |
| --- | --- |
| startup | `/actuator/health/liveness` |
| readiness | `/actuator/health/readiness` |
| liveness | `/actuator/health/liveness` |

The sample Deployment also sets `revisionHistoryLimit: 5`, RollingUpdate `maxUnavailable: 0` / `maxSurge: 1`, `minReadySeconds: 10`, `progressDeadlineSeconds: 120`, and `terminationGracePeriodSeconds: 45` and a 5s `preStop` sleep to align with `server.shutdown=graceful` and a 30s Spring lifecycle timeout (see [graceful-shutdown.md](../operations/graceful-shutdown.md)).

Starter resources: requests `100m` CPU / `256Mi` memory; limits `1` CPU / `512Mi` memory. With `readOnlyRootFilesystem: true`, the sample mounts `emptyDir` at `/tmp` for JVM temp files. Soft `topologySpreadConstraints` prefer spreading replicas across hosts when you scale out. See [`deploy/k8s/README.md`](../../deploy/k8s/README.md).

Treat them as **reference**. Optional client-side validation:

```bash
kubectl apply --dry-run=client -f deploy/k8s/
```

Do **not** expect this repository’s CI to run `kubectl apply` or to create Azure resources.

## Namespace quotas

Operator-owned `ResourceQuota` / `LimitRange` guidance (not shipped as YAML): [namespace-quotas.md](namespace-quotas.md).

## Config vs secrets

See [config-secrets.md](config-secrets.md) for ConfigMap vs Secret patterns and a future External Secrets note. `secret.example.yaml` is a placeholder shape only.

## AKS path (documented target)

1. Build and push an image to a registry you control.
2. Edit `deploy/k8s/deployment.yaml` `image` (and namespace/resources as needed).
3. Apply with `kubectl` (or GitOps) against **your** AKS cluster using credentials that never enter this git repo.
4. Optionally adapt `deploy/k8s/ingress.yaml` (controller class, host, TLS Secret)—still operator-owned; not applied by CI.

| Step | In this repo? |
| --- | --- |
| Runnable Spring Boot app | Yes |
| Dockerfile (multi-stage, non-root) | Yes |
| CI docker build (no push) | Yes |
| Sample Deployment / Service / ConfigMap | Yes — **reference** |
| Live `kubectl apply` from CI | **No** — not claimed |
| Sample Ingress YAML | Yes — **reference** (needs controller + host/TLS) |
| cert-manager, production sizing | Operator / later ADR |

## What “done” does not mean

Having sample YAML does **not** mean an AKS cluster exists, the image was pushed, or traffic is serving from Azure.

## Forwarded headers (Ingress / LB)

`server.forward-headers-strategy=framework` lets Spring honor `X-Forwarded-*` from a trusted proxy (Ingress, Azure LB, or local reverse proxy) when building redirects and absolute URLs. Only enable this when a proxy sits in front; do not expose the app directly to the internet without a hop that strips client-supplied forward headers.
