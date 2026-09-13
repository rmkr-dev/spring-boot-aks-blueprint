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

Pin an annotated tag (`v0.2.0` or later). Bump deliberately when the reusable contract changes; do not float this consumer on `@main`.

Caller contract: `pom.xml` at repository root (or under `working-directory`).

Optional inputs available on `@v0.2.0` (defaults are fine here): `timeout-minutes`, `fail-fast`, `enable-maven-cache`, `maven-goals`.

## Permissions

Default workflow `permissions: contents: read`. No secrets are required for the default green path.
