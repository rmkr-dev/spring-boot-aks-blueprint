# Architecture diagram

## Foundation slice

This slice has documentation surfaces only. A Mermaid diagram of the running system belongs when the Spring Boot application (and later container/k8s boundaries) exist. Until then, the diagram below is an explicit **placeholder of doc surfaces**, not a fake application topology.

```mermaid
flowchart TB
  subgraph docs_surfaces["This repo today (foundation)"]
    README["README.md"]
    AGENTS["AGENTS.md"]
    Contrib["CONTRIBUTING.md"]
    Arch["docs/architecture/"]
    Dec["docs/decisions/"]
    Dev["docs/development/"]
    Deploy["docs/deployment/"]
    Sec["docs/security/"]
  end

  subgraph planned["Planned slices (not present yet)"]
    App["src/ Spring Boot 3 + Java 21"]
    CI[".github/workflows CI"]
    K8s["deploy/k8s reference manifests"]
  end

  README --> Arch
  AGENTS --> Dev
  planned -.->|"later PRs"| docs_surfaces
```

When the application slice lands, replace this figure with components that exist in the tree (Application, controller, Actuator, container). When the k8s slice lands, show the AKS boundary as **aspirational / reference** unless a real apply is documented.
