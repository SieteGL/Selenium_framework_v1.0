package cl.consultor.qa.hooks;

import cl.consultor.qa.config.ConfigLoader;
import cl.consultor.qa.config.FrameworkConfig;
import cl.consultor.qa.driver.DriverManager;
import cl.consultor.qa.utils.ArtifactPaths;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Hooks {
    private static final Logger LOG = LoggerFactory.getLogger(Hooks.class);
    private FrameworkConfig config;

    @Before
    public void startScenario(Scenario scenario) {
        config = ConfigLoader.load();
        LOG.info("Execution snapshot | frameworkVersion={} | runId={} | environment={} | browser={} | headless={} | executionMode={} | baseUrl={} | timeout={} seconds",
                System.getProperty("framework.version", "unknown"), ArtifactPaths.runId(), config.environment(), config.browser(), config.headless(), config.executionMode(), config.baseUrl(), config.timeout().toSeconds());
        LOG.info("Scenario started: '{}'", scenario.getName());
        DriverManager.start(config);
    }

    @After
    public void finishScenario(Scenario scenario) {
        try {
            if (scenario.isFailed() && DriverManager.hasDriver()) captureFailureEvidence(scenario);
        } finally {
            DriverManager.quit();
            LOG.info("Driver context released for scenario '{}'", scenario.getName());
            LOG.info("Fin: '{}' | estado={}", scenario.getName(), scenario.getStatus());
        }
    }

    private void captureFailureEvidence(Scenario scenario) {
        try {
            byte[] bytes = ((TakesScreenshot) DriverManager.getDriver()).getScreenshotAs(OutputType.BYTES);
            String name = scenario.getName().replaceAll("[^a-zA-Z0-9-_]", "_") + "_" + config.browser() + "_" + config.environment() + "_" + ArtifactPaths.runId();
            Path target = ArtifactPaths.screenshotsDirectory().resolve(name + ".png");
            Files.createDirectories(target.getParent());
            Files.write(target, bytes);
            scenario.attach(bytes, "image/png", name);
            LOG.error("Screenshot de fallo: {}", target.toAbsolutePath());
        } catch (Exception exception) {
            LOG.error("Could not capture failure evidence; driver cleanup will continue.", exception);
        }
    }
}
