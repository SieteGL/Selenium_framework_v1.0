# qa-web-automation - Fase 2

> Template version: **0.4.1**. Maven `pom.xml` is the source of truth for the version.

## Quick start: new project

1. Clone this template and open `pom.xml` in IntelliJ as a Maven project.
2. Verify Java 21 and Maven 3.9+ with `java -version` and `mvn -version`.
3. Update the environment `baseUrl` values under `src/test/resources/config`.
4. Replace or intentionally retain the public demo Features, Pages, Steps and demo data.
5. Add business Features under `resources/features`, Pages under `pages` and Steps under `steps`.
6. Run `mvn clean test -Dheadless=true`, then `mvn test '-Dcucumber.filter.tags=@smoke'`.
7. Inspect `target/cucumber-reports`, `target/surefire-reports` and `target/logs`.

The bundled Sauce Demo credentials are explicitly identified as **public demo data only**, so a fresh clone can execute the reference implementation. Never commit real passwords, tokens, API keys or corporate credentials. Enterprise projects must provide secrets through environment variables and later CI/CD secret management.

`qa`, `staging` and `prod-smoke` are demonstration profiles that currently use the public demo URL; they are not real Sauce Demo environments. QA remains the safe default. A real project must replace each URL, and must configure `prod-smoke` explicitly according to its production policy.

Normally do **not** change `ConfigResolver`, `DriverFactory`, `DriverManager`, hooks, evidence paths or logging infrastructure when starting a business project. See [architecture](docs/architecture.md), [compatibility](docs/compatibility.md), [constitution](docs/framework-constitution.md), [checklists](docs/template-checklist.md), [Jenkins CI](docs/jenkins-ci.md) and [contributing](CONTRIBUTING.md).

Framework local de automatización Web con Java 21, Maven, Selenium, Cucumber y JUnit 5. La demo usa Sauce Demo; su URL
es externa y configurable.

## Arquitectura

```text
External executor (Maven / IntelliJ / Jenkins)
        | parameters
ConfigLoader -> ConfigResolver -> immutable FrameworkConfig
        |                         |
        +--> DriverFactory <-------+--> Hooks -> DriverManager (ThreadLocal)
                 |                              |
              Selenium Manager                  +--> Page Objects -> Selenium -> Browser

Feature -> Step Definitions -> Pages / Components -> DriverManager
```

Features, steps y pages no conocen IntelliJ, Jenkins, Docker, Grid ni el sistema operativo. `DriverFactory` es el único
punto que instancia `ChromeDriver` o `FirefoxDriver`; Selenium Manager obtiene el ejecutable compatible sin drivers en
el repositorio.

## Execution contract

Precedencia única: **JVM system property > environment variable > environment file > default file**. La unidad de
`TIMEOUT` es segundos. `prod-smoke` nunca es el default.

| Parámetro      | Propiedad JVM                     | Variable         | Valores                       | Default                 |
|----------------|-----------------------------------|------------------|-------------------------------|-------------------------|
| Environment    | `-Denv=qa`                        | `ENV`            | `qa`, `staging`, `prod-smoke` | `qa`                    |
| Browser        | `-Dbrowser=chrome`                | `BROWSER`        | `chrome`, `firefox`           | `chrome`                |
| Headless       | `-Dheadless=true`                 | `HEADLESS`       | `true`, `false`               | `false`                 |
| Base URL       | `-DbaseUrl=https://...`           | `BASE_URL`       | URL HTTP(S) absoluta          | archivo del environment |
| Timeout        | `-Dtimeout=10`                    | `TIMEOUT`        | entero positivo en segundos   | `10`                    |
| Execution mode | `-DexecutionMode=local`           | `EXECUTION_MODE` | `local`                       | `local`                 |
| Cucumber tags  | `-Dcucumber.filter.tags="@smoke"` | `TAGS`*          | expresión Cucumber            | todos                   |

`-Dcucumber.filter.tags` es el mecanismo canónico. `TAGS` queda reservado para un futuro adaptador de CI; por ahora use
la propiedad oficial para no duplicar el filtro de Cucumber.

Los archivos están en `src/test/resources/config`: `default.properties` contiene valores comunes y cada environment solo
su `baseUrl`. No almacene secretos allí. Configuraciones inválidas fallan antes de abrir el navegador:
browser/environment no soportado, boolean inválido, timeout inválido, URL inválida y `executionMode=remote` producen
mensajes claros. La ejecución remota está reservada, no hace fallback silencioso a local.

## Ejecución

En PowerShell:

```powershell
cd .\qa-web-automation
mvn clean test
mvn clean test -Denv=staging -Dbrowser=chrome -Dheadless=true
mvn clean test '-Dcucumber.filter.tags=@smoke and @critical'

$env:ENV='qa'; $env:BROWSER='firefox'; $env:HEADLESS='true'
mvn clean test
Remove-Item Env:ENV,Env:BROWSER,Env:HEADLESS
```

Use `-D...` para una ejecución aislada y reproducible (ideal para Maven/Jenkins). Use variables de entorno para una
sesión de terminal, secretos o Docker. En CMD: `set BROWSER=firefox && mvn clean test`.

## Ciclo de vida, evidencias y reportes

Cada `@Before` resuelve y valida `FrameworkConfig`, registra el snapshot sin secretos y crea un driver. Cada `@After`
captura evidencia en caso de fallo, adjunta el PNG a Cucumber, ejecuta `quit()` y remueve el contexto incluso si la
captura falla. `ThreadLocal` asigna un driver/config por hilo: hoy evita estado global compartido y mañana permitirá
paralelizar sin reescribir tests; la paralelización no está activa.

- `target/cucumber-reports/cucumber.html`: reporte HTML Cucumber.
- `target/surefire-reports/TEST-*.xml`: XML JUnit publicado por Jenkins.
- `target/screenshots/<runId>/`: capturas únicas por ejecución fallida.
- `target/logs/framework.log`: lifecycle, configuración efectiva y errores sin secretos.

Las assertions permanecen visibles en los steps y los waits explícitos usan el `Duration` tipado de `FrameworkConfig`.
No se utiliza `Thread.sleep` ni se crean drivers desde Pages/Steps.

## Jenkins CI baseline

El repositorio incluye un `Jenkinsfile` declarativo y parametrizado que ejecuta el mismo Execution Contract de Maven: primero los self-tests del framework y después la suite UI seleccionada. `TEST_SUITE` se traduce a `-Dcucumber.filter.tags=@<suite>`; `BASE_URL_OVERRIDE`, si se entrega, se inyecta como `BASE_URL` y nunca como texto de comando.

La baseline requiere que el agente Jenkins ya disponga de Java 21, Maven 3.9+ y el browser local elegido. No instala Jenkins ni incorpora Docker, Grid o `RemoteWebDriver`. Consulte la [guía Jenkins CI](docs/jenkins-ci.md) para parámetros, setup, equivalencia local, evidencia y límites de alcance.

## Dependencias y plugins Maven

Las **dependencies** son Selenium, Cucumber, JUnit Platform y Logback, usadas por el código de pruebas. El único **Maven
plugin** es Surefire, que ejecuta `mvn test` y escribe XML JUnit. Se mantienen Selenium 4.49.0, Cucumber 7.33.0, JUnit
5.14.4 / Platform 1.14.4 y Surefire 3.6.0; no se agregó TestNG ni Failsafe.

## Troubleshooting

- `Unsupported browser`: use `chrome` o `firefox`; Edge se agrega exclusivamente en `DriverFactory` en otra fase.
- `Unsupported environment`: use `qa`, `staging` o `prod-smoke`.
- Firefox/Chrome no disponible: instale el browser; Selenium Manager gestiona el driver, no el browser.
- `BASE_URL` inválida o inalcanzable: corrija la URL/entorno y compruebe conectividad.
- `TIMEOUT` inválido: use un entero positivo, por ejemplo `-Dtimeout=15`.
- Cucumber encuentra 0 escenarios: revise la expresión de tags y `src/test/resources/features`.
- Para diagnóstico, consulte logs, screenshots, HTML Cucumber y XML Surefire en `target/`.

## Preparado para fases posteriores

El contrato externo, `FrameworkConfig`, `DriverFactory` y `DriverManager` permiten añadir `ExecutionMode.REMOTE` y
`RemoteWebDriver` en una fase Grid sin cambiar features, steps ni pages. Esta fase implementa una baseline Jenkins local;
no implementa Docker, Grid, RemoteWebDriver funcional, instalación/configuración de un servidor Jenkins, ALM, Allure ni
paralelización activa.

## Calidad interna y diseño de pruebas

`Requirement -> Risk -> Technique -> Scenario -> Test data + Tags -> Steps -> Pages / Components -> Driver -> Browser -> Assertions -> Evidence`

Una **Page** representa una pantalla; un **Component** representa UI reutilizable. `NavigationMenu` es un component real. No existe estado mutable compartido: cada scenario recibe driver y configuración mediante `ThreadLocal`. Un `ScenarioContext` solo se agregará si un futuro flujo necesita compartir datos tipados dentro del mismo scenario; nunca como mapa global.

Los locators priorizan `id` y `data-test`. Los waits distinguen presence (DOM), visibility (visible) y clickable (interactuable). Pages leen/interactúan; las assertions de negocio permanecen en Steps e informan esperado y recibido. Consulte [estrategia](docs/test-strategy.md), [convenciones](docs/coding-conventions.md) y [guía anti-flaky](docs/flaky-tests.md).
