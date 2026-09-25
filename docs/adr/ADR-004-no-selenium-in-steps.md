# ADR-004: No Selenium in Steps

Status: Accepted

## Context

Technical locators in Gherkin bindings obscure behavior and duplicate UI logic.

## Decision

Steps express behavior and delegate to Pages or Components.

## Consequences

UI changes remain localized outside Features and business-readable Steps.
