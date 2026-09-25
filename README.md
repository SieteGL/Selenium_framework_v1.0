# qa-web-automation - Fase 1

Framework local de automatización web con Java, Maven, Cucumber, JUnit 5 y Selenium. Demuestra login, validación de
error y navegación contra [Sauce Demo](https://www.saucedemo.com/), una aplicación pública que podría cambiar o dejar de
estar disponible. La URL no está acoplada al código.

## Stack y versiones verificadas (25-09-2026)

| Elemento                              |         Versión | Motivo                                                                                |
|---------------------------------------|----------------:|---------------------------------------------------------------------------------------|
| Java                                  |          21 LTS | JDK local, LTS actual                                                                 |
| Maven                                 |          3.9.16 | Instalado localmente                                                                  |
| Selenium Java                         |          4.49.0 | WebDriver moderno con Selenium Manager incluido                                       |
| Cucumber Java / JUnit Platform Engine |          7.33.0 | Última línea 7.x compatible con JUnit 5; mismo número para todos los módulos Cucumber |
| JUnit Jupiter / Platform              | 5.14.4 / 1.14.4 | API de assertions y suite JUnit 5                                                     |
| Maven Surefire                        |           3.6.0 | Ejecuta JUnit Platform y escribe XML JUnit                                            |
| SLF4J Simple                          |          2.0.17 | Logging sencillo sin configuración pesada                                             |

La versión de Selenium se validó en Maven Central; Cucumber en su documentación oficial. Cucumber 8.0.1 requiere JUnit
Platform 6 y presentó un conflicto de dependencias transitivas al resolverse con Maven, por lo que se fija 7.33.0 para
respetar el requisito de JUnit 5. Surefire se validó en Apache Maven. Una **dependencia** es una librería que usa el
código de pruebas. Un **plugin Maven** ejecuta una fase del build: aquí Surefire ejecuta `test`.

## Requisitos y apertura en IntelliJ

Instale JDK 21 y Maven 3.9+. Abra `pom.xml` como proyecto Maven y permita que IntelliJ importe las dependencias. Plugins
recomendados del IDE: **Gherkin** y **Cucumber for Java**. Son ayudas de edición y ejecución en IntelliJ; no sustituyen
ni son dependencias Maven. El proyecto se ejecuta íntegramente por terminal con Maven.

## Estructura

```text
src/test/java/cl/consultor/qa/
  runners/      Suite JUnit Platform que descubre Cucumber
  steps/        Lenguaje de negocio que delega, sin findElement
  pages/        Page Objects de Login e Inventario
  components/   UI reutilizable: NavigationMenu
  driver/       Creación y ciclo de vida centralizado de WebDriver
  hooks/        Inicio, limpieza y evidencia de fallo
  config/       Resolución externa de configuración
  utils/        Waits explícitos y lectura de datos de ejemplo
src/test/resources/
  features/     Especificaciones Gherkin
  config/       Defaults y endpoints por ambiente
  testdata/     Datos públicos de demostración
```

Flujo: `Feature -> Step Definition -> Page Object / Component -> Selenium WebDriver -> Browser`.

Un Page Object representa una página coherente. Un Component Object representa una pieza reutilizable de UI;
`NavigationMenu` puede vivir en más de una página.

## Configuración

`config/config.properties` guarda defaults. `ENV` elige `config/<env>.properties` (`qa`, `staging`, `prod-smoke`). Se
resuelve así: propiedad JVM (`-D...`) -> variable de entorno -> archivo -> default. Las variables son `ENV`, `BASE_URL`,
`BROWSER`, `HEADLESS`, `TIMEOUT`; no agregue secretos al repositorio. Los datos actuales son públicos de Sauce Demo
únicamente.

Los selectores priorizan `id` y `data-test`; se evita XPath frágil. `Waits` centraliza espera explícita de visibilidad y
clickeabilidad: `presence` solo confirma presencia DOM, `visibility` exige que sea visible y `clickable` añade que pueda
interactuarse. No se usa `Thread.sleep`.

## Ejecución desde Windows PowerShell

```powershell
cd .\qa-web-automation
mvn clean test
$env:BROWSER='firefox'; mvn clean test
$env:HEADLESS='true'; mvn clean test
$env:ENV='staging'; mvn clean test
mvn clean test '-Dcucumber.filter.tags=@smoke'
mvn clean test '-Dcucumber.filter.tags=@regression and @critical'
```

Para limpiar la variable de la sesión: `Remove-Item Env:BROWSER`. En CMD use `set BROWSER=firefox && mvn clean test`.
También puede usar propiedades JVM, por ejemplo `mvn test -Dbrowser=firefox -Dheadless=true`.

Tags no son categorías exclusivas: el escenario principal es simultáneamente `@smoke`, `@regression` y `@critical`.

## Evidencias y reportes

Después de ejecutar, consulte:

- `target/cucumber-reports/cucumber.html`: reporte HTML de Cucumber.
- `target/surefire-reports/TEST-*.xml`: XML JUnit para una futura integración Jenkins.
- `target/screenshots/`: PNG único por escenario fallido, adjuntado además al escenario Cucumber.
- consola Maven: logging de inicio, entorno, browser, URL, error y cierre.

Selenium Manager se incluye con Selenium: al construir `ChromeDriver` o `FirefoxDriver`, Selenium resuelve el driver
compatible sin `chromedriver.exe` ni rutas hardcodeadas dentro del proyecto.

`DriverFactory` es el único lugar que crea drivers locales. `DriverManager` usa `ThreadLocal`: hoy evita estado global
mal compartido; en una futura ejecución paralela conserva un driver por hilo/escenario. Para Grid solo se reemplazaría
esta fábrica por una estrategia `RemoteWebDriver`, sin reescribir features, steps o pages.

## Cómo extenderlo

Para una Feature nueva, cree un `.feature` funcional bajo `resources/features`, añada steps pequeños y delegue a
Pages/Components. Para una Page nueva, cree una clase con locators estables y operaciones de negocio. Para Edge, agregue
sus opciones y el `case "edge"` solo en `DriverFactory`. Para un ambiente, agregue `config/<ambiente>.properties`;
Jenkins podrá inyectar sus valores por variables de entorno.

JUnit 5 es el framework/runner elegido. TestNG puede ser válido en otro proyecto, pero no es necesario para Cucumber ni
se mezcla aquí. Failsafe tampoco se usa: en esta fase toda la suite vive en `mvn test`.

## Roadmap (no implementado)

1. Fase 2: Git y estrategia de repositorio.
2. Fase 3: Dockerización.
3. Fase 4: Selenium Grid y navegadores Docker.
4. Fase 5: Jenkins Pipeline.
5. Fase 6: CI/CD, smoke, regression, E2E y quality gates.
6. Fase 7: ALM / Test Management.
7. Fase 8: paralelización avanzada y cross-browser.
8. Fase 9: reporting y observabilidad empresariales.

No se implementan deliberadamente Jenkins, Docker, Grid, RemoteWebDriver, CI/CD, ALM, Kubernetes, TestNG ni Failsafe. La
separación de configuración, factory y resultados deja puntos de extensión claros para esas fases.
