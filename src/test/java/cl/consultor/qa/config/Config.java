package cl.consultor.qa.config;

public record Config(String environment, String baseUrl, String browser, boolean headless, long timeoutSeconds) {
}
