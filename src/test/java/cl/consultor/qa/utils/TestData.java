package cl.consultor.qa.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class TestData {
    private static final Properties DATA = load();

    private TestData() {
    }

    public static String value(String key) {
        String environmentValue = System.getenv(environmentKey(key));
        if (environmentValue != null && !environmentValue.isBlank()) return environmentValue;
        String value = DATA.getProperty(key);
        if (value == null || value.isBlank()) throw new IllegalStateException("Missing required test data key: " + key);
        return value;
    }

    private static String environmentKey(String key) {
        return "QA_" + key.toUpperCase().replace('.', '_');
    }

    private static Properties load() {
        Properties properties = new Properties();
        try (InputStream input = TestData.class.getClassLoader().getResourceAsStream("testdata/credentials.properties")) {
            if (input == null) throw new IllegalStateException("No se encontró testdata/credentials.properties");
            properties.load(input);
            return properties;
        } catch (IOException exception) {
            throw new IllegalStateException("No fue posible cargar datos de prueba", exception);
        }
    }
}
