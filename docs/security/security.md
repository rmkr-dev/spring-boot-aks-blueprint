# Security

Security posture for this blueprint and for anyone adapting it.

## Defaults in this tree

- **No secrets in git.** Tokens, kubeconfigs, `.env` files, and connection strings stay out of the tree and out of examples.
- **No fake auth.** Do not add placeholder JWT/OAuth that looks finished but is not.
- **Public-safe content.** No personal contact details, no company names.
- **Least privilege workflows.** [`.github/workflows/ci.yml`](../../.github/workflows/ci.yml) sets `permissions: contents: read`. No secrets required for the default green path.
- **Containers.** Dockerfile runs as non-root `app` user.
- **Kubernetes samples.** ConfigMaps for non-secret config only (`deploy/k8s/`). Document Secrets out of band; never commit Secret data.
- **Actuator.** Only `health`, `info`, and `metrics` are exposed; health details stay `when_authorized`.
- **API errors.** Validation failures return Problem Details without stack traces (see [api-errors.md](../development/api-errors.md)).

## Dependencies

Dependabot updates Maven and GitHub Actions weekly. Spring Boot **major** bumps (for example 3.x → 4.x) are ignored so the blueprint stays on 3.5.x until an explicit ADR.

## Reporting

See root [SECURITY.md](../../SECURITY.md). Do not paste secrets into issues; rotate first, then describe the class of leak.

## Kubernetes samples

Reference manifests under `deploy/k8s/` use a non-root securityContext, drop all capabilities, read-only root filesystem, a dedicated ServiceAccount with token automount disabled, and optional NetworkPolicy. They are not applied by CI—review before use in any cluster.

