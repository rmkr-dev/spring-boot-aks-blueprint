# Contributing

Thanks for improving this blueprint. Keep changes small, complete, and honest about what the repo actually contains.

## Read first

1. [AGENTS.md](AGENTS.md) — guardrails, definition of done, and self-review.
2. [docs/development/development.md](docs/development/development.md) — how work is sequenced.
3. The docs that your change touches under `docs/`.

## How to propose a change

1. Branch from `main`.
2. Implement **one slice**. Update every layer that slice needs (code, tests, docs, CI, manifests). Do not leave a half-described feature.
3. Use a conventional commit message (`docs:`, `feat:`, `chore:`, `fix:`). Write it as if a colleague will read it in `git log` a year from now. No tool or agent co-author footers.
4. Open a pull request against `main`. Say what slice this is, what is in scope, and what is explicitly out of scope.

## What we will not merge

- Speculative extras (auth frameworks, databases, Node/npm) that the slice did not ask for
- Docs that describe live AKS deployment, CI, or product behavior that is not in the tree
- Secrets, personal contact details, or company-specific branding
- Drive-by reformatting of files you did not otherwise change

Questions about process belong in the pull request. Architecture-level choices belong in an ADR — see [docs/decisions/README.md](docs/decisions/README.md) when that folder exists, or add it with the decision.
