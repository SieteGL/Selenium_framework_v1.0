package cl.consultor.qa.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * The only configuration class that reads JVM properties, OS variables and classpath files.
 */
public final class ConfigLoader {
    private ConfigLoader() {
    }

    public static FrameworkConfig load() {
        Map<EnvironmentType, Properties> environmentFiles = new EnumMap<>(EnvironmentType.class);
        for (EnvironmentType environment : EnvironmentType.values())
            environmentFiles.put(environment, loadResource("config/" + environment.fileName() + ".properties"));
        Map<String, String> system = new HashMap<>();
        System.getProperties().forEach((key, value) -> system.put(String.valueOf(key), String.valueOf(value)));
        return new ConfigResolver(system, System.getenv(), loadResource("config/default.properties"), environmentFiles).resolve();
    }

    private static Properties loadResource(String resource) {
        Properties properties = new Properties();
        try (InputStream input = ConfigLoader.class.getClassLoader().getResourceAsStream(resource)) {
            if (input == null) throw new ConfigurationException("Missing required configuration resource: " + resource);
            properties.load(input);
            return properties;
        } catch (IOException exception) {
            throw new ConfigurationException("Could not read configuration resource: " + resource);
        }
    }
}
