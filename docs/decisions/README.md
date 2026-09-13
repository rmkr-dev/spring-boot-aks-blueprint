# Architecture Decision Records

Significant choices are recorded here so a later reader can see *what* we decided, *why*, and *what we rejected*.

## When to write an ADR

Write one when the decision is expensive to reverse or will constrain later work, for example:

- Choosing language, framework, or container base
- Choosing AKS (or another orchestrator) as the documented deploy target
- Changing CI system or required checks
- Adding auth, a database, or a public API shape

Skip an ADR for typo fixes, doc wording, and other local edits that do not change the system’s shape.

## How to record one

1. Add `ADR-NNN-short-title.md` in this directory. Keep numbers monotonic.
2. Use Context, Options, Decision, Rationale, Consequences / Trade-offs.
3. Link the ADR from the PR and from `docs/architecture/architecture.md` if the current-state design changed.
4. Do not rewrite history. Supersede the old ADR and leave it in place.

## Index

| ADR | Title | Status |
| --- | --- | --- |
| *(none yet)* | First ADR expected with the CI / architecture slice | — |
