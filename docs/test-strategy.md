# Test strategy

`Requirement -> risk -> technique -> scenario -> automation -> evidence`.

| Scenario | Risk | Technique | Tags | Reason |
|---|---|---|---|---|
| Valid login and menu access | Legitimate user cannot access | Positive flow/state transition | `@smoke @regression @critical @e2e` | Fast critical business gate |
| Unknown credentials rejected | Incorrect credentials gain access | Equivalence partitioning/negative | `@sanity @regression @negative @e2e` | Invalid credential partition |
| Required data rejected | Empty credentials are accepted | Negative/decision branch | `@sanity @regression @negative @e2e` | Required-data branch |

Smoke is small, fast and critical. Regression is broader. Tags overlap by design; `@e2e` is applied only to the valid flow that crosses authentication, navigation and inventory UI. The negative scenarios are intentionally not E2E. The demo has no documented numeric rule, so boundary testing is not fabricated. UI automation is one layer of the test pyramid; deterministic rules should move to unit/component/API tests when available.

Configuration is execution behavior; test data is scenario input; secrets must use environment/CI secret stores and never logs. Gherkin Examples hold readable negative partitions; reusable public demo credentials remain in properties.
