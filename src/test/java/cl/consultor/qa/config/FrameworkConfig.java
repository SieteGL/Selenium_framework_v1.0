package cl.consultor.qa.config;

import java.net.URI;
import java.time.Duration;

/**
 * Immutable, validated configuration for one framework execution.
 */
public record FrameworkConfig(EnvironmentType environment, URI baseUrl, BrowserType browser,
                              boolean headless, Duration timeout, ExecutionMode executionMode) {
}
