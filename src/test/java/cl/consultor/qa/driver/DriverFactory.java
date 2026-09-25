package cl.consultor.qa.driver;

import cl.consultor.qa.config.Config;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

/**
 * Single local-driver creation point; RemoteWebDriver can be added here in a later Grid phase.
 */
public final class DriverFactory {
    private DriverFactory() {
    }

    public static WebDriver create(Config config) {
        return switch (config.browser()) {
            case "chrome" -> new ChromeDriver(chromeOptions(config.headless()));
            case "firefox" -> new FirefoxDriver(firefoxOptions(config.headless()));
            default ->
                    throw new IllegalArgumentException("Browser no soportado: " + config.browser() + ". Use chrome o firefox.");
        };
    }

    private static ChromeOptions chromeOptions(boolean headless) {
        ChromeOptions options = new ChromeOptions();
        if (headless) options.addArguments("--headless=new");
        options.addArguments("--window-size=1440,900");
        return options;
    }

    private static FirefoxOptions firefoxOptions(boolean headless) {
        FirefoxOptions options = new FirefoxOptions();
        if (headless) options.addArguments("-headless");
        return options;
    }
}
