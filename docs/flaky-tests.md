# Flaky test prevention

A flaky test passes and fails without an intentional product change. Investigate timing, state leakage, selectors, browser differences, network latency, animations, cookies/storage, locale and data collisions before changing a test.

This framework uses a fresh driver per scenario, `ThreadLocal` context, stable locators, explicit waits and unique evidence paths. The current scenarios start new browser sessions, so explicit cookie/storage clearing is unnecessary. Do not add retries as the first solution: they can hide genuine defects. Reproduce, inspect `target/logs`, screenshots and reports, then correct the root cause.
