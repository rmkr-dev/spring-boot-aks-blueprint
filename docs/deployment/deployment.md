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

## Container image

Dockerfile and local container run instructions land in the observability/Docker slice. Until then, run the JAR or `spring-boot:run` as above.

## AKS path (documented target)

Intended operator flow when you choose to deploy:

1. Build and push an image to a registry you control (after Dockerfile lands).
2. Review sample manifests under `deploy/k8s/` when that slice lands—treat them as **reference**.
3. Adjust namespace, image, probes, and resources for your cluster.
4. Apply with `kubectl` (or GitOps) against **your** AKS cluster using credentials that never enter this git repo.
5. Optionally add Ingress / TLS / DNS—not shipped as a completed story here.

| Step | In this repo? |
| --- | --- |
| Runnable Spring Boot app | Yes |
| Dockerfile | Planned — next slice |
| Sample Deployment / Service / ConfigMap | Planned — k8s slice |
| Live `kubectl apply` from CI | **No** — not claimed |
| Ingress, cert-manager, production sizing | Operator / later ADR |

## What “done” does not mean

Having a local app does **not** mean an AKS cluster exists, an image was pushed, or traffic is serving from Azure.
