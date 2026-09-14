# Continuous integration

## Workflow

[`.github/workflows/ci.yml`](../../.github/workflows/ci.yml) runs on pushes to `main` and on pull requests.

| Job | What it does |
| --- | --- |
| `test` | Calls reusable `java-maven-ci.yml@v0.2.0` — Temurin 21, `mvn -B test` |
| `docker` | Builds the multi-stage image locally on the runner (**no registry push**) |

## Reusable workflow pin

```yaml
uses: rmkr-dev/gha-reusable-workflows/.github/workflows/java-maven-ci.yml@v0.2.0
```

**Verified for this wave:** `v0.2.0` is the newest annotated tag on `rmkr-dev/gha-reusable-workflows` (also `v0.1.0`). Pin an annotated tag; bump deliberately when the reusable contract changes; do not float this consumer on `@main`.

Caller contract: `pom.xml` at repository root (or under `working-directory`).

Optional inputs available on `@v0.2.0` (defaults are fine here): `timeout-minutes`, `fail-fast`, `enable-maven-cache`, `maven-goals`.

## Permissions

Default workflow `permissions: contents: read`. No secrets are required for the default green path.

## Concurrency

The workflow uses a concurrency group per ref with `cancel-in-progress: true` so newer pushes to the same PR or branch cancel superseded runs.

## Dependabot

GitHub Actions updates are grouped (`groups.github-actions`) in `.github/dependabot.yml`. Maven updates group Spring Boot minor/patch separately from other dependencies; Spring Boot **major** upgrades remain ignored. Checkout stays pinned at `actions/checkout@v7` in `ci.yml`.
