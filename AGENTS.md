# Agent and human guardrails

Read this file before you change this repository. These rules apply to people and to coding agents.

## Before you write code

1. Read `README.md`, this file, and the docs that touch your change (`docs/architecture/`, `docs/decisions/`, `docs/development/`, `docs/deployment/`, `docs/security/`).
2. State the slice you are completing. If the work is larger than one reviewable PR, split it.
3. Prefer the smallest change that is **correct and complete** for that slice. Do not “while you’re here” refactor.

## Human-first

- Write for a reviewer who was not in the session: commit messages, PR body, and docs must stand alone.
- Do not leave the human to reverse-engineer intent from a dump of files.
- High-impact changes (license, security defaults, CI required checks, public API, data handling, cluster credentials) need **human approval** before merge. An agent may draft; a person decides.

## Complete across layers

A slice is not done if you updated only one layer. When the change needs them, update together:

- behavior (code)
- tests
- CI / repo automation
- docs that describe the behavior
- architecture or an ADR, if the shape of the system changed
- deployment docs/manifests, if the runtime shape changed
- optional scripts under `scripts/` only when documented and referenced from README/docs

Do not add a file that nothing references, or a doc that describes a file that does not exist.

## No orphans, no speculation, no fakes

- **No orphans:** every new doc, script, or workflow has a reader and a reason.
- **No speculative engineering:** do not add frameworks, auth stacks, databases, or “future-proof” abstractions for slices that are not in scope.
- **No fake implementations:** no stub endpoints that pretend to be production auth, no CI badges for workflows that are not running, no architecture diagrams of systems that are not in this repo.
- **Docs match reality.** If AKS deploy is documented as a *path* and manifests are *reference*, say so. Do not claim a live cluster apply happened.

## Tests and CI are first-class

When application code exists, tests ship in the same PR as the behavior. CI is the default way those tests run.

- **GitHub Actions first.** Prefer workflows in this repo that mirror the contract of `rmkr-dev/gha-reusable-workflows` (Java Maven: setup Temurin 21, `mvn -B test`). Switch to `uses: rmkr-dev/gha-reusable-workflows/...` when that reusable workflow is ready and documented.
- **GitHub Free-first.** Do not depend on paid GitHub features or third-party secrets for the default green pipeline.
- Do not claim CI exists before the workflow file is merged.

## Security by default

- No secrets in the repo, in examples, or in commit messages.
- No personal contact details or company names in content.
- Least privilege for tokens and workflow permissions when those files exist.
- Kubernetes manifests must not embed credentials; use ConfigMaps for non-secret config and document Secret usage without shipping secret values.

## Docs and architecture

- User-facing or contributor-facing behavior needs a doc update in the same PR.
- System shape lives in `docs/architecture/`. Significant choices get an ADR under `docs/decisions/`.
- Deployment path lives in `docs/deployment/`. Keep “implemented vs target” language accurate.
- Network diagrams belong once the service has a network story; label aspirational AKS boundaries clearly.

## Definition of done

A change is done when all of the following are true:

- [ ] Scope matches the agreed slice; nothing extra landed “for later convenience”
- [ ] Behavior, tests, automation, and docs that this slice requires are present and consistent
- [ ] No secrets, personal contact details, or leftover placeholders that claim to be finished
- [ ] `README.md` and any linked docs still describe the repo as it is
- [ ] Significant decisions have an ADR
- [ ] High-impact items are called out for a human reviewer
- [ ] You could merge this PR and leave the repo coherent if no further PR ever shipped
- [ ] `mvn test` passes when application code is in scope

## Final self-review

Before you ask for review:

1. Diff the PR as a stranger. Is anything unexplained?
2. Grep for names of files you added. Are they linked from `README.md` or the right `docs/` index?
3. Confirm you did **not** add Node/npm unless an ADR requires it.
4. Confirm commit messages are conventional (`feat:`, `docs:`, `chore:`, …) and read like a person wrote them—no Cursor/AI co-author trailers.
5. Re-read this file and `docs/development/development.md`. Fix anything that now contradicts them.
