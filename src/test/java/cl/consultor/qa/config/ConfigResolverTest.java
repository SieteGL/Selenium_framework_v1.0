package cl.consultor.qa.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.util.EnumMap;
import java.util.Map;
import java.util.Properties;

import org.junit.jupiter.api.Test;

class ConfigResolverTest {
    @Test
    void defaultsProduceValidQaConfiguration() {
        FrameworkConfig config = resolver(Map.of(), Map.of()).resolve();
        assertEquals(EnvironmentType.QA, config.environment());
        assertEquals(BrowserType.CHROME, config.browser());
        assertEquals(Duration.ofSeconds(10), config.timeout());
    }

    @Test
    void systemPropertyHasPriorityOverEnvironmentVariableAndFile() {
        FrameworkConfig config = resolver(Map.of("browser", "firefox", "timeout", "7"), Map.of("BROWSER", "chrome", "TIMEOUT", "20")).resolve();
        assertEquals(BrowserType.FIREFOX, config.browser());
        assertEquals(Duration.ofSeconds(7), config.timeout());
    }

    @Test
    void invalidValuesFailWithActionableMessages() {
        assertMessage(Map.of("browser", "safari"), "Unsupported browser");
        assertMessage(Map.of("timeout", "hola"), "TIMEOUT must be a positive integer");
        assertMessage(Map.of("headless", "abc"), "HEADLESS must be either true or false");
        assertMessage(Map.of("env", "production"), "Unsupported environment");
        assertMessage(Map.of("executionMode", "remote"), "Remote execution is reserved");
        assertMessage(Map.of("baseUrl", "not a url"), "BASE_URL must be a valid");
    }

    private void assertMessage(Map<String, String> system, String expectedFragment) {
        assertTrue(assertThrows(ConfigurationException.class, () -> resolver(system, Map.of()).resolve()).getMessage().contains(expectedFragment));
    }

    private ConfigResolver resolver(Map<String, String> system, Map<String, String> environment) {
        Properties defaults = new Properties();
        defaults.put("env", "qa");
        defaults.put("browser", "chrome");
        defaults.put("headless", "false");
        defaults.put("timeout", "10");
        defaults.put("executionMode", "local");
        Properties qa = new Properties();
        qa.put("baseUrl", "https://qa.example.test/");
        Properties staging = new Properties();
        staging.put("baseUrl", "https://staging.example.test/");
        Properties prod = new Properties();
        prod.put("baseUrl", "https://prod.example.test/");
        Map<EnvironmentType, Properties> environments = new EnumMap<>(EnvironmentType.class);
        environments.put(EnvironmentType.QA, qa);
        environments.put(EnvironmentType.STAGING, staging);
        environments.put(EnvironmentType.PROD_SMOKE, prod);
        return new ConfigResolver(system, environment, defaults, environments);
    }
}
