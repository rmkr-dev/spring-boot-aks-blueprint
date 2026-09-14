# Contributing

Thanks for improving this blueprint. Keep changes small, complete, and honest about what the repo actually contains.

## Read first

1. [AGENTS.md](AGENTS.md) — guardrails, definition of done, and self-review.
2. [docs/development/development.md](docs/development/development.md) — how work is sequenced.
3. The docs that your change touches under `docs/`.

## How to propose a change

1. Branch from `main`.
2. Implement **one slice** with **multiple focused commits** (not one giant commit). Update every layer that slice needs (code, tests, docs, CI, manifests).
3. Use a conventional commit message (`docs:`, `feat:`, `chore:`, `test:`, `ci:`). Write it as if a colleague will read it in `git log` a year from now. No tool or agent co-author footers.
4. Open a pull request against `main`. Say what slice this is, what is in scope, and what is explicitly out of scope.
5. Wait for CI green (`test` via reusable workflow `@v0.2.0`, plus docker build).

## What we will not merge

- Speculative extras (auth frameworks, databases, Node/npm, springdoc without tests) that the slice did not ask for
- Docs that describe live AKS deployment or product behavior that is not in the tree
- Secrets, personal contact details, or company-specific branding
- Unsolicited Spring Boot 4 major upgrades (Dependabot ignores them; change needs an ADR)
- Drive-by reformatting of files you did not otherwise change

Questions about process belong in the pull request. Architecture-level choices belong in an ADR — see [docs/decisions/](docs/decisions/).

## Local checks beyond `mvn test`

- Optional load smoke: `scripts/load-hello.sh` or `scripts/load-hello.py` (see [load-hello.md](docs/development/load-hello.md)).
- Graceful shutdown behavior: [graceful-shutdown.md](docs/operations/graceful-shutdown.md).
- Container health: Dockerfile/Compose HEALTHCHECK on Actuator liveness ([docker-healthcheck.md](docs/deployment/docker-healthcheck.md)).
