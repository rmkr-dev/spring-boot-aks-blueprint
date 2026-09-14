# ConfigMap and Secret patterns

How this blueprint separates non-secret configuration from credentials. **No real secrets are committed.**

## ConfigMap (shipped)

[`deploy/k8s/configmap.yaml`](../../deploy/k8s/configmap.yaml) holds non-secret env:

| Key | Purpose |
| --- | --- |
| `SPRING_APPLICATION_NAME` | Spring application name |
| `SERVER_PORT` | HTTP listen port |

The Deployment mounts it with `envFrom.configMapRef`. Override locally with process env or Compose `environment:` — same variable names.

## Secret (pattern only — not applied)

Use a Kubernetes Secret for credentials (registry pull, TLS key material, API tokens). A **placeholder** shape lives at [`deploy/k8s/secret.example.yaml`](../../deploy/k8s/secret.example.yaml):

- File name ends in `.example.yaml` so operators copy-adapt out of band.
- Values are empty placeholders — never paste live tokens into git.
- Prefer `stringData` only in private scratch files; for git-friendly docs keep keys listed without values, or use sealed/external stores.

Wire a Secret into the Deployment with `envFrom.secretRef` or individual `secretKeyRef` entries when you adapt the sample. Do **not** open a PR that adds real `data:` / `stringData:` payloads.

## External Secrets (future)

[External Secrets Operator](https://external-secrets.io/) (or Azure Key Vault Provider for Secrets Store CSI) can sync vault material into Kubernetes Secrets without storing ciphertext in this repo. This blueprint does **not** ship an `ExternalSecret` CRD sample yet—treat it as a future ADR when a vault backend is chosen.

## Checklist

- [ ] Non-secret defaults stay in ConfigMap / `application.yml` / env
- [ ] Credentials stay in cluster Secrets, sealed secrets, or a vault — never in git
- [ ] TLS for Ingress uses a Secret created out of band (see `ingress.yaml` comments)
- [ ] CI and Dependabot need no cluster credentials for the default green path

See also [security.md](../security/security.md) and [`deploy/k8s/README.md`](../../deploy/k8s/README.md).
