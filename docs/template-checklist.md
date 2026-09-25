# Template and release checklists

## New project

- [ ] Rename Maven artifact/name if required.
- [ ] Replace demo URLs in environment files.
- [ ] Remove or retain demo Features, Pages, Steps and public demo test data intentionally.
- [ ] Create the first business Feature, Page and Steps.
- [ ] Configure non-secret test data; inject secrets through environment/CI.
- [ ] Run `mvn clean test` and the Smoke suite.
- [ ] Verify a real Git clone runs without ignored or workspace-local files.
- [ ] Identify bundled demo data and keep real secrets external.

## Template release

- [ ] Run `mvn clean test`.
- [ ] Run framework self-tests and Chrome Smoke.
- [ ] Run Firefox Smoke when available.
- [ ] Check headless mode, README and compatibility matrix.
- [ ] Add an ADR for an architectural decision.
- [ ] Increment Semantic Versioning appropriately.
