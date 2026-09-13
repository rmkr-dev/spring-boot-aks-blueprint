# Continuous integration

## Workflow

[`.github/workflows/ci.yml`](../../.github/workflows/ci.yml) runs on pushes to `main` and on pull requests.

| Job | What it does |
| --- | --- |
| `test` | Calls reusable `java-maven-ci.yml` — Temurin 21, `mvn -B test` |
| `docker` | Builds the multi-stage image locally on the runner (**no registry push**) |

## Reusable workflow pin

```yaml
uses: rmkr-dev/gha-reusable-workflows/.github/workflows/java-maven-ci.yml@v0.1.0
```

Pin the annotated tag (`v0.1.0`). Bump deliberately when the reusable contract changes; do not float on `@main` for this consumer.

Caller contract: `pom.xml` at repository root (or under `working-directory`).

## Permissions

Default workflow `permissions: contents: read`. No secrets are required for the default green path.
