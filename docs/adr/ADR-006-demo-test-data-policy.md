# ADR-006: Demo test data policy

Status: Accepted

## Context

The reference implementation must run immediately after a Git clone.

## Decision

Version only public Sauce Demo credentials required by the demo, clearly marked as demo data. Real passwords, tokens and API keys remain prohibited from source control.

## Consequences

The demo is reproducible; enterprise projects must inject secrets through environment variables or CI/CD secret management.
