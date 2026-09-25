# Coding conventions

- Steps contain no Selenium locators, waits or driver creation.
- Only `DriverFactory` creates drivers; `DriverManager` owns scenario-local context.
- Pages are screens; Components are reusable UI parts. Neither selects browser/environment.
- Prefer `id`, then stable `data-test`, then CSS; avoid absolute XPath and positional selectors.
- Use explicit waits through `Waits`; never use `Thread.sleep`.
- Pages read/interact; test layers own business assertions.
- No hardcoded BASE_URL, secrets, mutable shared state or scenario dependencies.
