# ADR-001: JUnit 5

Status: Accepted

## Context

Cucumber needs one test-platform integration.

## Decision

Use JUnit 5/JUnit Platform and `cucumber-junit-platform-engine`; do not add TestNG.

## Consequences

Surefire discovers the JUnit Platform suite consistently from Maven and IntelliJ.
