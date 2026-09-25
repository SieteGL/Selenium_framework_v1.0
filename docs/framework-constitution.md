# Framework constitution

1. Preserve `Feature -> Steps -> Page/Component -> Core -> Selenium`.
2. Never put Selenium locators, waits or driver creation in Steps.
3. Only `DriverFactory` creates drivers; Pages never read OS configuration.
4. Keep the Execution Contract backward compatible unless a documented major-version change is required.
5. Do not hardcode URLs, real secrets or personal paths.
6. Keep scenarios independent and evidence under `target/`.
7. New core behavior requires self-tests, documentation and smoke verification.
