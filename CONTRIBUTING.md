# Contributing

Run `mvn clean test -Dheadless=true` before committing. Core changes must preserve the Execution Contract, add focused self-tests when appropriate, update documentation and not introduce secrets, personal paths, `Thread.sleep` or Selenium in Steps. Never commit real secrets; public demo credentials must be explicitly marked as demo data.

Place business pages, steps and data in the project layer. Place browser/configuration/lifecycle changes in the framework core. Add a short ADR when a decision changes the architecture or public contract.
