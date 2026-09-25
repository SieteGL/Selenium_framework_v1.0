package cl.consultor.qa.driver;

import cl.consultor.qa.config.FrameworkConfig;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Single local-driver creation point; RemoteWebDriver can be added here in a later Grid phase.
 */
public final class DriverFactory {
    private static final Logger LOG = LoggerFactory.getLogger(DriverFactory.class);

    private DriverFactory() {
    }

    public static WebDriver create(FrameworkConfig config) {
        LOG.info("Creating {} driver through Selenium Manager", config.browser());
        return switch (config.browser()) {
            case CHROME -> new ChromeDriver(chromeOptions(config.headless()));
            case FIREFOX -> new FirefoxDriver(firefoxOptions(config.headless()));
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
