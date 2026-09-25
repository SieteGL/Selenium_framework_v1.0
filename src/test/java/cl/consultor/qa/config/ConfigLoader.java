package cl.consultor.qa.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Properties;

/**
 * Resolves system property, then environment variable, then config file, then a safe default.
 */
public final class ConfigLoader {
    private static final Properties DEFAULTS = load("config/config.properties");

    private ConfigLoader() {
    }

    public static Config load() {
        String environment = value("ENV", "env", DEFAULTS.getProperty("env", "qa"));
        Properties environmentProperties = load("config/" + environment + ".properties");
        String baseUrl = value("BASE_URL", "base.url", environmentProperties.getProperty("base.url", DEFAULTS.getProperty("base.url")));
        String browser = value("BROWSER", "browser", DEFAULTS.getProperty("browser", "chrome")).toLowerCase(Locale.ROOT);
        boolean headless = Boolean.parseBoolean(value("HEADLESS", "headless", DEFAULTS.getProperty("headless", "false")));
        long timeout = Long.parseLong(value("TIMEOUT", "timeout.seconds", DEFAULTS.getProperty("timeout.seconds", "10")));
        return new Config(environment, baseUrl, browser, headless, timeout);
    }

    private static String value(String environmentName, String systemProperty, String fallback) {
        String systemValue = System.getProperty(systemProperty);
        if (systemValue != null && !systemValue.isBlank()) return systemValue;
        String environmentValue = System.getenv(environmentName);
        return environmentValue == null || environmentValue.isBlank() ? fallback : environmentValue;
    }

    private static Properties load(String resource) {
        Properties properties = new Properties();
        try (InputStream input = ConfigLoader.class.getClassLoader().getResourceAsStream(resource)) {
            if (input != null) properties.load(input);
            return properties;
        } catch (IOException exception) {
            throw new IllegalStateException("No fue posible leer " + resource, exception);
        }
    }
}
