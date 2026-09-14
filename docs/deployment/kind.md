# kind / local Kubernetes notes

Honest notes for trying the **reference** manifests on a local cluster. This repository does **not** create a kind cluster, install CNI plugins, or run `kubectl apply` from CI.

## Prefer Compose for the app alone

If you only need the HTTP service locally, use Compose (no cluster):

```bash
docker compose up --build
```

See [deployment.md](deployment.md) and root `compose.yaml`.

## Optional: kind

If you already use [kind](https://kind.sigs.k8s.io/) (or minikube), you can dry-run or apply the samples **on your machine**:

```bash
# Validate only (safe; no cluster required beyond kubectl client)
kubectl apply --dry-run=client -f deploy/k8s/

# Optional: load a locally built image into kind, then apply (operator steps)
docker build -t spring-boot-aks-blueprint:local .
kind load docker-image spring-boot-aks-blueprint:local
# edit deploy/k8s/deployment.yaml image if needed, then:
# kubectl apply -f deploy/k8s/
```

Caveats:

- Image name in `deployment.yaml` defaults to `spring-boot-aks-blueprint:local` with `imagePullPolicy: IfNotPresent`—suitable for kind after `kind load`.
- HPA needs metrics-server (not installed by this repo).
- NetworkPolicy needs a CNI that enforces policies (kind’s default varies by version/setup).
- `ingress.yaml` is a reference sample only—still requires a controller and is not applied by this repo's CI. TLS and public DNS remain out of scope.

## What this repo claims

| Action | In repo? |
| --- | --- |
| `compose.yaml` for the app | Yes |
| Sample manifests under `deploy/k8s/` | Yes — reference |
| Automate kind create / apply in CI | **No** |
| Prove a live AKS cluster | **No** |

## Manifest inventory (reference)

When experimenting on kind, the samples under `deploy/k8s/` include Deployment/Service/ConfigMap, optional HPA/NetworkPolicy/PDB/ServiceAccount/Ingress, and `secret.example.yaml` (shape only). Prefer Compose for day-to-day app runs; use kind only when you need Kubernetes API objects. Graceful shutdown and Docker/Compose HEALTHCHECK still apply to the container image you load into the cluster.
