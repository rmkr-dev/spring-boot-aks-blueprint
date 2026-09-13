# Security

Security posture for this blueprint and for anyone adapting it.

## Defaults

- **No secrets in git.** Tokens, kubeconfigs, `.env` files, and connection strings stay out of the tree and out of examples.
- **No fake auth.** Do not add placeholder JWT/OAuth that looks finished but is not.
- **Public-safe content.** No personal contact details, no company names, no certifications in repo content.
- **Least privilege.** When workflows appear, grant `permissions` explicitly and keep `GITHUB_TOKEN` write access off unless the job must push or comment.
- **Containers.** Prefer non-root runtime users in the Dockerfile when that file exists.
- **Kubernetes.** ConfigMaps for non-secret config only. Document how Secrets would be supplied; never commit Secret data.

## Dependencies

Every dependency is an attack surface. Dependabot for Maven and GitHub Actions belongs with the CI slice. Do not paste security badges before scans or updates actually run.

## Reporting

Open a GitHub issue describing the unsafe default or doc. Do not paste secrets into the issue; rotate first, then describe the class of leak. A root `SECURITY.md` can be added in the CI hygiene slice for GitHub’s reporting UI.
