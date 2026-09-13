# Kubernetes samples (reference only)

These manifests illustrate a minimal Deployment, Service, and ConfigMap for the Spring Boot app.

**They are not applied by CI and are not proof of a live AKS cluster.**

Suggested local review (optional):

```bash
kubectl apply --dry-run=client -f deploy/k8s/
```

Replace the container `image` with one you built and pushed. Do not commit secrets; use Kubernetes Secrets (out of band) for credentials.
