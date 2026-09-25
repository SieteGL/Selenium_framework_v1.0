package cl.consultor.qa.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class TestData {
    private static final Properties DATA = load();

    private TestData() {
    }

    public static String value(String key) {
        return DATA.getProperty(key);
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
