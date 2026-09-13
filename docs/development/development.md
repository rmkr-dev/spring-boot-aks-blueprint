# Development

How people (and agents) work on this blueprint.

## Prerequisites

| Need | Tools |
| --- | --- |
| Build and test | JDK 21, Maven 3.9+ |
| Docs-only edits | Git, GitHub account |
| Container (later slice) | Docker (or compatible build) |

Do not add Node/npm unless an ADR requires it.

## Branching and review

- Branch from `main`. One concern per branch and pull request.
- Conventional commits (`docs:`, `feat:`, `chore:`, `test:`). No Cursor/AI co-author trailers.
- PRs say what slice landed and what is still out of scope.
- High-impact changes wait for human approval. See [AGENTS.md](../../AGENTS.md).

## Working on a slice

1. Read [AGENTS.md](../../AGENTS.md) and the docs you will touch.
2. Change the smallest set of files that leaves the repo consistent.
3. Update indexes (`README.md`, folder READMEs) when you add or remove docs.
4. If the decision is significant, add an ADR under `docs/decisions/`.
5. Run `mvn -B test` before opening a PR that touches application code.

## Local verification

```bash
mvn -B test
mvn -B spring-boot:run
curl -s http://localhost:8080/api/v1/hello
curl -s 'http://localhost:8080/api/v1/hello?name=AKS'
```

`mvn -B test` runs unit and WebMvc tests under `src/test/java`. It must pass for any PR that changes application code.

## Remaining planned slices

- Actuator exposure tuning + multi-stage Dockerfile
- GitHub Actions CI, Dependabot, templates
- Architecture/network diagram polish, ADR, reference k8s manifests
