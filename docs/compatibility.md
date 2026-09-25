# Compatibility matrix

| Template | Java | Maven | Selenium | Cucumber | JUnit Platform | Surefire |
|---|---|---|---|---|---|---|
| 0.4.1 | 21 LTS | 3.9.16 | 4.49.0 | 7.33.0 | 1.14.4 / Jupiter 5.14.4 | 3.6.0 |

Versions are pinned in `pom.xml` for reproducible builds. Selenium Manager is supplied by Selenium; no driver executable is committed. Cucumber 7.33.0 is retained because this template uses JUnit 5 rather than Cucumber 8's JUnit Platform 6 line.
