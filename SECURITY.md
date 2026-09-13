# Security policy

## Supported versions

This is a personal blueprint. Security fixes land on `main` for the latest published slice only.

## Reporting a vulnerability

Please open a **private** GitHub security advisory on this repository if the vulnerability UI is available, or open a GitHub issue **without** including exploit details or secrets if that is the only channel.

Do not commit credentials, tokens, kubeconfigs, or `.env` files. Sample Kubernetes manifests must stay reference-only and secret-free.

## Scope

In scope: dependency issues in this repository, workflow supply-chain concerns, and unsafe defaults in sample manifests or Dockerfile.

Out of scope: third-party AKS clusters you deploy yourself, and speculative “future” features not present in the tree.
