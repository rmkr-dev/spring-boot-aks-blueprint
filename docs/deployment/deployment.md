# Deployment

Honest deployment path for this blueprint. **Nothing here claims that a production (or any) AKS cluster was applied from this repository.**

## Local process

```bash
mvn -B test
mvn -B spring-boot:run
# or
mvn -B package && java -jar target/spring-boot-aks-blueprint-0.1.0-SNAPSHOT.jar
```

Default listen port: `8080` (override with `SERVER_PORT`).

Useful endpoints:

- API: `http://localhost:8080/api/v1/hello`
- Health: `http://localhost:8080/actuator/health`
- Readiness (K8s): `http://localhost:8080/actuator/health/readiness`
- Liveness (K8s): `http://localhost:8080/actuator/health/liveness`
- Metrics: `http://localhost:8080/actuator/metrics`
- Info: `http://localhost:8080/actuator/info`

## Container image

```bash
docker build -t spring-boot-aks-blueprint:local .
docker run --rm -p 8080:8080 spring-boot-aks-blueprint:local
```

Or with Compose (app only — no database service):

```bash
docker compose up --build
```

This uses root [`compose.yaml`](../../compose.yaml) to build the Dockerfile and publish port `8080`. Stop with `Ctrl+C` or `docker compose down`. Compose does **not** create a Kubernetes cluster; for cluster experiments use your own kind/minikube and the reference manifests under `deploy/k8s/` (still not applied by this repo's CI).

CI builds the same image on pull requests and `main` pushes **without pushing** to a registry. See [ci.md](../development/ci.md).

## Reference Kubernetes manifests

Samples live under [`deploy/k8s/`](../../deploy/k8s/):

| File | Purpose |
| --- | --- |
| `configmap.yaml` | Non-secret env (`SPRING_APPLICATION_NAME`, `SERVER_PORT`) |
| `deployment.yaml` | Single replica, non-root securityContext, Actuator probes, CPU/memory requests/limits |
| `service.yaml` | ClusterIP Service on port 80 → container 8080 |
| `hpa.yaml` | Optional CPU HPA (1–3 replicas @ 70% average utilization) — needs metrics-server |

Probe paths match Actuator when `management.endpoint.health.probes.enabled=true`:

| Probe | Path |
| --- | --- |
| startup | `/actuator/health/liveness` |
| readiness | `/actuator/health/readiness` |
| liveness | `/actuator/health/liveness` |

Starter resources: requests `100m` CPU / `256Mi` memory; limits `1` CPU / `512Mi` memory. See [`deploy/k8s/README.md`](../../deploy/k8s/README.md).

Treat them as **reference**. Optional client-side validation:

```bash
kubectl apply --dry-run=client -f deploy/k8s/
```

Do **not** expect this repository’s CI to run `kubectl apply` or to create Azure resources.

## AKS path (documented target)

1. Build and push an image to a registry you control.
2. Edit `deploy/k8s/deployment.yaml` `image` (and namespace/resources as needed).
3. Apply with `kubectl` (or GitOps) against **your** AKS cluster using credentials that never enter this git repo.
4. Optionally add Ingress / TLS / DNS—not shipped as a completed story here.

| Step | In this repo? |
| --- | --- |
| Runnable Spring Boot app | Yes |
| Dockerfile (multi-stage, non-root) | Yes |
| CI docker build (no push) | Yes |
| Sample Deployment / Service / ConfigMap | Yes — **reference** |
| Live `kubectl apply` from CI | **No** — not claimed |
| Ingress, cert-manager, production sizing | Operator / later ADR |

## What “done” does not mean

Having sample YAML does **not** mean an AKS cluster exists, the image was pushed, or traffic is serving from Azure.
