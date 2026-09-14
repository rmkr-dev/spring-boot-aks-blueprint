# Namespace quotas (operator notes)

This blueprint does **not** ship `ResourceQuota` or `LimitRange` objects. Cluster admins often apply those at the namespace level for multi-tenant AKS. Use this page as a checklist when adapting the sample Deployment.

## Why they are out of tree

- Quotas are environment-specific (CPU/memory pool, object counts, storage classes).
- Shipping sample quotas that fail `kubectl apply --dry-run` against empty namespaces is more noise than help.
- The Deployment already declares container requests/limits; quotas should be set to allow those values.

## Suggested operator steps

1. Create or choose a namespace for the app.
2. Apply a `LimitRange` so bare pods get default requests/limits consistent with `deploy/k8s/deployment.yaml` (`100m`/`256Mi` requests, `1`/`512Mi` limits as a starting point).
3. Apply a `ResourceQuota` that can host at least HPA `maxReplicas` (sample: 3) times the per-pod requests.
4. Confirm the ServiceAccount, NetworkPolicy, and PDB still fit object-count quotas if you set them.

## Relation to this repo

| Concern | In repo? |
| --- | --- |
| Container requests/limits | Yes — `deployment.yaml` |
| HPA max replicas | Yes — `hpa.yaml` (1–3) |
| Namespace ResourceQuota / LimitRange | **No** — operator-owned |
| Live AKS apply | **No** |

Tune numbers for your node pool; do not copy production quotas from this personal blueprint.
