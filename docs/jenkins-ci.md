# Jenkins CI baseline

`Jenkinsfile` is a declarative, parameterized baseline for this QA repository. It runs on `agent any` and assumes the selected Jenkins agent already has Java 21, Maven 3.9+ and the requested local browser installed and reachable on `PATH`. Selenium Manager resolves the compatible browser driver at execution time.

It deliberately does not install Jenkins, Docker, Grid, RemoteWebDriver or a browser. `EXECUTION_MODE` is limited to `local`, matching the current framework contract.

## Pipeline flow

1. **Checkout** explicitly obtains the repository (`skipDefaultCheckout` prevents an implicit duplicate checkout).
2. **Environment / Tool Info** records Java and Maven versions in the build log.
3. **Framework Self Tests** runs `ConfigResolverTest` and `ArtifactPathsTest` after `mvn clean`. These deterministic tests validate the QA framework itself and do not open a browser.
4. **QA Selected Suite** runs only `RunCucumberTest` and maps `TEST_SUITE` to `-Dcucumber.filter.tags=@<suite>`.
5. The `post { always }` block publishes JUnit XML and archives Cucumber reports, logs and screenshots even when Maven fails. Maven failures still fail the build.

The pipeline disables concurrent builds and has a 30-minute global timeout. The workspace is reset for test output through `mvn clean`; no Workspace Cleanup plugin is required.

## Parameters and contract mapping

| Jenkins parameter | Accepted values | Framework input |
|---|---|---|
| `ENV` | `qa`, `staging`, `prod-smoke` | `-Denv` |
| `BROWSER` | `chrome`, `firefox` | `-Dbrowser` |
| `HEADLESS` | `true`, `false` | `-Dheadless` |
| `TEST_SUITE` | `smoke`, `regression`, `e2e`, `negative`, `sanity` | `-Dcucumber.filter.tags=@<suite>` |
| `EXECUTION_MODE` | `local` | `-DexecutionMode` |
| `BASE_URL_OVERRIDE` | optional HTTP(S) URL | `BASE_URL` environment variable |

An empty `BASE_URL_OVERRIDE` leaves the URL from `src/test/resources/config/<env>.properties` intact. A supplied URL is validated by `ConfigResolver`; it is injected as an environment variable rather than shell text.

## Jenkins agent setup

Configure an agent with the following available from `PATH`:

- Java 21 (`java -version`)
- Maven 3.9 or newer (`mvn -version`)
- Chrome for `chrome` builds and/or Firefox for `firefox` builds

Create a Pipeline job that points at the repository and uses **Pipeline script from SCM**. Jenkins will discover `Jenkinsfile` at the repository root. The baseline uses common Pipeline steps (`checkout`, `junit`, `archiveArtifacts`); ensure the normal Jenkins Pipeline and JUnit support is available in the controller.

For the current demo, `qa`, `staging` and `prod-smoke` all target public Sauce Demo placeholders. Before connecting an enterprise environment, replace those URLs and inject real secrets through Jenkins credentials or the organization's secret manager—never through this repository, build parameters or logs.

## Local equivalent

Use the same two commands before relying on a Jenkins agent:

```powershell
mvn clean test '-Dtest=ConfigResolverTest,ArtifactPathsTest'
mvn test '-Dtest=RunCucumberTest' -Denv=qa -Dbrowser=chrome -Dheadless=true -DexecutionMode=local '-Dcucumber.filter.tags=@smoke'
```

To emulate `BASE_URL_OVERRIDE`, set `BASE_URL` only for the invocation/session. In PowerShell:

```powershell
$env:BASE_URL='https://example.test'
mvn test '-Dtest=RunCucumberTest' -Denv=qa -Dbrowser=chrome -Dheadless=true -DexecutionMode=local '-Dcucumber.filter.tags=@smoke'
Remove-Item Env:BASE_URL
```

## Scope boundaries

The three test categories are intentionally different:

| Category | Owner / repository | Typical command | Purpose |
|---|---|---|---|
| Product unit tests | Product application repository | Product build command | Validate application business logic without UI automation. They are not part of this QA repository. |
| QA framework self-tests | This repository | `-Dtest=ConfigResolverTest,ArtifactPathsTest` | Validate configuration precedence, validation and artifact path behavior. |
| UI QA tests | This repository | `-Dtest=RunCucumberTest` plus a Cucumber tag | Validate browser behavior through Features, Steps and Pages. |

This split uses Surefire's existing test selector; it does not introduce Maven modules, Failsafe, Docker or a second test architecture.

## Evidence and result semantics

- `target/surefire-reports/TEST-*.xml` is published with `junit`.
- `target/cucumber-reports/**`, `target/logs/**` and `target/screenshots/**` are archived if present.
- A failing self-test prevents the UI stage from running and fails the build.
- A failing UI test also fails the build, while its available reports and screenshots are still retained by `post { always }`.
