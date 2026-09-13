# Development

How people (and agents) work on this blueprint.

## Prerequisites

| Slice | Tools |
| --- | --- |
| Docs only (this foundation) | Git, GitHub account |
| Application (later) | JDK 21, Maven 3.9+ |
| Container (later) | Docker (or compatible build) |
| k8s reference (later) | Optional: `kubectl` for local validation — **not** required to merge |

Do not add Node/npm unless an ADR requires it.

## Branching and review

- Branch from `main`. One concern per branch and pull request.
- Conventional commits (`docs:`, `feat:`, `chore:`, `fix:`). Messages should read as if a person typed them; no Cursor/AI co-author trailers.
- PRs say what slice landed and what is still out of scope.
- High-impact changes wait for human approval. See [AGENTS.md](../../AGENTS.md).

## Working on a slice

1. Read [AGENTS.md](../../AGENTS.md) and the docs you will touch.
2. Change the smallest set of files that leaves the repo consistent.
3. Update indexes (`README.md`, folder READMEs) when you add or remove docs.
4. If the decision is significant, add an ADR under `docs/decisions/`.
5. When application code exists: run `mvn -B test` before opening the PR.

## Expected slice order

1. Foundation docs + `AGENTS.md` (this slice)
2. Spring Boot app + unit tests + Dockerfile
3. Architecture/network diagrams, ADR, CI, Dependabot, templates
4. Reference k8s manifests + deployment doc update

## Local verification (foundation)

- Links in `README.md` and `docs/` resolve to files that exist.
- No doc claims `src/`, workflows, or a live AKS apply.
