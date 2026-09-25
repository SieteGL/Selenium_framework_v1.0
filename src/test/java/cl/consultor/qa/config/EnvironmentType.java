package cl.consultor.qa.config;

import java.util.Locale;

public enum EnvironmentType {
    QA, STAGING, PROD_SMOKE;

    static EnvironmentType parse(String value) {
        try {
            return valueOf(value.trim().toUpperCase(Locale.ROOT).replace('-', '_'));
        } catch (RuntimeException exception) {
            throw new ConfigurationException("Unsupported environment: " + value + ". Supported environments: qa, staging, prod-smoke.");
        }
    }

    String fileName() {
        return name().toLowerCase(Locale.ROOT).replace('_', '-');
    }
}
