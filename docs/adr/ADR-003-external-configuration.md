# ADR-003: External configuration

Status: Accepted

## Context

Local and future CI executions need identical commands with different values.

## Decision

Resolve JVM property > environment variable > environment configuration > default.

## Consequences

Jenkins/Docker can inject parameters without changing business code.
