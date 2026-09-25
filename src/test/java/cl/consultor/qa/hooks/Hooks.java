package cl.consultor.qa.hooks;

import cl.consultor.qa.config.Config;
import cl.consultor.qa.config.ConfigLoader;
import cl.consultor.qa.driver.DriverManager;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Hooks {
    private static final Logger LOG = LoggerFactory.getLogger(Hooks.class);
    private static final DateTimeFormatter STAMP = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss-SSS");
    private Config config;

    @Before
    public void startScenario(Scenario scenario) {
        config = ConfigLoader.load();
        LOG.info("Inicio: '{}' | env={} | browser={} | headless={} | url={}", scenario.getName(), config.environment(), config.browser(), config.headless(), config.baseUrl());
        DriverManager.start(config);
    }

    @After
    public void finishScenario(Scenario scenario) {
        try {
            if (scenario.isFailed()) captureFailureEvidence(scenario);
        } finally {
            DriverManager.quit();
            LOG.info("Fin: '{}' | estado={}", scenario.getName(), scenario.getStatus());
        }
    }

    private void captureFailureEvidence(Scenario scenario) {
        byte[] bytes = ((TakesScreenshot) DriverManager.getDriver()).getScreenshotAs(OutputType.BYTES);
        String name = scenario.getName().replaceAll("[^a-zA-Z0-9-_]", "_") + "-" + STAMP.format(LocalDateTime.now());
        Path target = Path.of("target", "screenshots", name + ".png");
        try {
            Files.createDirectories(target.getParent());
            Files.write(target, bytes);
            scenario.attach(bytes, "image/png", name);
            LOG.error("Screenshot de fallo: {}", target.toAbsolutePath());
        } catch (IOException exception) {
            LOG.error("No se pudo guardar la evidencia de fallo", exception);
        }
    }
}
