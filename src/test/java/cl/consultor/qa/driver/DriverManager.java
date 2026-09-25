package cl.consultor.qa.driver;

import cl.consultor.qa.config.Config;
import org.openqa.selenium.WebDriver;

/**
 * ThreadLocal isolates a driver per scenario thread, ready for future parallel execution.
 */
public final class DriverManager {
    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();

    private DriverManager() {
    }

    public static void start(Config config) {
        DRIVER.set(DriverFactory.create(config));
    }

    public static WebDriver getDriver() {
        WebDriver driver = DRIVER.get();
        if (driver == null) throw new IllegalStateException("WebDriver no fue inicializado para este escenario.");
        return driver;
    }

    public static void quit() {
        WebDriver driver = DRIVER.get();
        try {
            if (driver != null) driver.quit();
        } finally {
            DRIVER.remove();
        }
    }
}
