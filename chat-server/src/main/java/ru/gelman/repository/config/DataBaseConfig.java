package ru.gelman.repository.config;

import lombok.Data;

import java.util.Properties;

@Data
public class DataBaseConfig {
    private final String url;
    private final String username;
    private final String password;

    public static DataBaseConfig from(Properties properties) {
        String url = properties.getProperty("url", "");
        String username = properties.getProperty("username", "");
        String password = properties.getProperty("password", "");
        return new DataBaseConfig(url, username, password);
    }
}
