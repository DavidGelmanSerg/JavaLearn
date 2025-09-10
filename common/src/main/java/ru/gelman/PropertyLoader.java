package ru.gelman;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyLoader {

    public static Properties load(String path) {
        try (InputStream in = PropertyLoader.class.getResourceAsStream(path)) {
            if (in != null) {
                Properties properties = new Properties();
                properties.load(in);
                return properties;
            }
            throw new RuntimeException("Property file not found");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
