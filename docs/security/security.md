# Security

Security posture for this blueprint and for anyone adapting it.

## Defaults in this tree

- **No secrets in git.** Tokens, kubeconfigs, `.env` files, and connection strings stay out of the tree and out of examples.
- **No fake auth.** Do not add placeholder JWT/OAuth that looks finished but is not.
- **Public-safe content.** No personal contact details, no company names.
- **Least privilege workflows.** [`.github/workflows/ci.yml`](../../.github/workflows/ci.yml) sets `permissions: contents: read`. No secrets required for the default green path.
- **Containers.** Dockerfile runs as non-root `app` user with fixed uid/gid **10001** (mirrored in the Deployment securityContext).
- **Kubernetes samples.** ConfigMaps for non-secret config only (`deploy/k8s/`). Secret **shape** in `secret.example.yaml`; never commit Secret data. See [config-secrets.md](../deployment/config-secrets.md).
- **Actuator.** Only `health`, `info`, `metrics`, and `prometheus` are exposed; health details stay `when_authorized`.
- **API errors.** Validation failures return Problem Details without stack traces (see [api-errors.md](../development/api-errors.md)).
- **HTTP security headers.** `SecurityHeadersFilter` sets `X-Content-Type-Options`, `X-Frame-Options`, `Referrer-Policy`, `Permissions-Policy`, and disables legacy `X-XSS-Protection` on responses. See [http-headers.md](http-headers.md).
- **Request correlation.** `RequestIdFilter` echoes or generates `X-Request-Id` (also MDC `requestId`). See [request-id.md](../operations/request-id.md).

## Dependencies

Dependabot updates Maven and GitHub Actions weekly. GitHub Actions updates are **grouped** into a single `github-actions` PR. Maven groups Spring Boot minor/patch separately from other libraries. Spring Boot **major** bumps (for example 3.x → 4.x) are ignored so the blueprint stays on 3.5.x until an explicit ADR. CI pins `actions/checkout@v7`.

## Reporting

See root [SECURITY.md](../../SECURITY.md). Do not paste secrets into issues; rotate first, then describe the class of leak.

## Kubernetes samples

Reference manifests under `deploy/k8s/` use a non-root securityContext (numeric uid/gid **10001**, aligned with the Dockerfile), drop all capabilities, read-only root filesystem (with an `emptyDir` mount at `/tmp` for JVM scratch), a dedicated ServiceAccount with token automount disabled, and optional NetworkPolicy. They are not applied by CI—review before use in any cluster.

