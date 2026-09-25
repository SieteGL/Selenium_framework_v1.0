package cl.consultor.qa.utils;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class ArtifactPaths {
    private static final String RUN_ID = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss").format(LocalDateTime.now());

    private ArtifactPaths() {
    }

    public static String runId() {
        return RUN_ID;
    }

    public static Path screenshotsDirectory() {
        return Path.of("target", "screenshots", RUN_ID);
    }

    public static Path logsDirectory() {
        return Path.of("target", "logs");
    }
}
