package cl.consultor.qa.driver;

import cl.consultor.qa.config.FrameworkConfig;
import org.openqa.selenium.WebDriver;

/**
 * ThreadLocal isolates a driver per scenario thread, ready for future parallel execution.
 */
public final class DriverManager {
    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();
    private static final ThreadLocal<FrameworkConfig> CONFIG = new ThreadLocal<>();

    private DriverManager() {
    }

    public static void start(FrameworkConfig config) {
        CONFIG.set(config);
        DRIVER.set(DriverFactory.create(config));
    }

    public static FrameworkConfig getConfig() {
        FrameworkConfig config = CONFIG.get();
        if (config == null) throw new IllegalStateException("FrameworkConfig no fue inicializado para este escenario.");
        return config;
    }

    public static boolean hasDriver() {
        return DRIVER.get() != null;
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
            CONFIG.remove();
        }
    }
}
