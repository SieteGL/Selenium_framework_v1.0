# ADR-002: Selenium Manager

Status: Accepted

## Context

Manual driver binaries create local-path and version drift.

## Decision

Use Selenium Manager through Selenium WebDriver; commit no driver executables.

## Consequences

Local browser availability remains a machine prerequisite, while driver resolution is automated.
