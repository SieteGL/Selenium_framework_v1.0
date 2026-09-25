package cl.consultor.qa.config;

import java.util.Locale;

public enum BrowserType {
    CHROME, FIREFOX;

    static BrowserType parse(String value) {
        try {
            return valueOf(value.trim().toUpperCase(Locale.ROOT));
        } catch (RuntimeException exception) {
            throw new ConfigurationException("Unsupported browser: " + value + ". Supported browsers: chrome, firefox.");
        }
    }
}
