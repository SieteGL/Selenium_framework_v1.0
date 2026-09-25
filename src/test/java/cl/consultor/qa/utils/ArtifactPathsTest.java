package cl.consultor.qa.utils;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class ArtifactPathsTest {
    @Test void artifactsStayUnderTargetAndRunIdIsStable() {
        assertTrue(ArtifactPaths.runId().matches("\\d{8}-\\d{6}"));
        assertTrue(ArtifactPaths.screenshotsDirectory().toString().contains(ArtifactPaths.runId()));
        assertTrue(ArtifactPaths.logsDirectory().toString().contains("target"));
    }
}
