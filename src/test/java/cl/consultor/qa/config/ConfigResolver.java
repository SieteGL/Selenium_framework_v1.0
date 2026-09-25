package cl.consultor.qa.config;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.util.Map;
import java.util.Properties;

/**
 * Resolves: JVM property > environment variable > environment file > default file.
 */
final class ConfigResolver {
    private final Map<String, String> system, environment;
    private final Properties defaults;
    private final Map<EnvironmentType, Properties> environments;

    ConfigResolver(Map<String, String> system, Map<String, String> environment, Properties defaults, Map<EnvironmentType, Properties> environments) {
        this.system = system;
        this.environment = environment;
        this.defaults = defaults;
        this.environments = environments;
    }

    FrameworkConfig resolve() {
        EnvironmentType env = EnvironmentType.parse(value("env", "ENV", null, defaults.getProperty("env", "qa")));
        Properties envFile = environments.get(env);
        if (envFile == null)
            throw new ConfigurationException("Missing configuration file for environment: " + env.fileName());
        return new FrameworkConfig(env, url(value("baseUrl", "BASE_URL", envFile, defaults.getProperty("baseUrl"))), BrowserType.parse(value("browser", "BROWSER", envFile, defaults.getProperty("browser", "chrome"))), bool(value("headless", "HEADLESS", envFile, defaults.getProperty("headless", "false"))), Duration.ofSeconds(timeout(value("timeout", "TIMEOUT", envFile, defaults.getProperty("timeout", "10")))), ExecutionMode.parse(value("executionMode", "EXECUTION_MODE", envFile, defaults.getProperty("executionMode", "local"))));
    }

    private String value(String sys, String os, Properties envFile, String fallback) {
        String v = system.get(sys);
        if (present(v)) return v;
        v = environment.get(os);
        if (present(v)) return v;
        v = envFile == null ? null : envFile.getProperty(sys);
        return present(v) ? v : fallback;
    }

    private boolean bool(String value) {
        if ("true".equalsIgnoreCase(value)) return true;
        if ("false".equalsIgnoreCase(value)) return false;
        throw new ConfigurationException("HEADLESS must be either true or false.");
    }

    private int timeout(String value) {
        try {
            int seconds = Integer.parseInt(value);
            if (seconds > 0) return seconds;
        } catch (NumberFormatException ignored) {
        }
        throw new ConfigurationException("TIMEOUT must be a positive integer expressed in seconds.");
    }

    private URI url(String value) {
        if (!present(value)) throw new ConfigurationException("BASE_URL must be a valid absolute HTTP or HTTPS URL.");
        try {
            URI uri = new URI(value);
            if (!uri.isAbsolute() || uri.getHost() == null || !("http".equalsIgnoreCase(uri.getScheme()) || "https".equalsIgnoreCase(uri.getScheme())))
                throw new ConfigurationException("BASE_URL must be a valid absolute HTTP or HTTPS URL.");
            return uri;
        } catch (URISyntaxException exception) {
            throw new ConfigurationException("BASE_URL must be a valid absolute HTTP or HTTPS URL.");
        }
    }

    private boolean present(String value) {
        return value != null && !value.isBlank();
    }
}
