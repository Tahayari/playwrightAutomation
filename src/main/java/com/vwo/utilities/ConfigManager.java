package com.vwo.utilities;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * ConfigManager loads and provides access to application configuration properties
 * from the environment.properties file
 */
public class ConfigManager {
    private static ConfigManager instance;
    private Properties properties;

    private ConfigManager() {
        loadProperties();
    }

    /**
     * Get singleton instance of ConfigManager
     */
    public static ConfigManager getInstance() {
        if (instance == null) {
            instance = new ConfigManager();
        }
        return instance;
    }

    /**
     * Loads properties from environment.properties file
     */
    private void loadProperties() {
        properties = new Properties();
        try (InputStream input = ConfigManager.class.getClassLoader().getResourceAsStream("environment.properties")) {
            if (input == null) {
                throw new RuntimeException("environment.properties file not found in resources folder");
            }
            properties.load(input);

            // Validate required properties
            if (getProperty("app.url") == null || getProperty("app.username") == null || getProperty("app.password") == null) {
                throw new RuntimeException("Missing required properties in environment.properties");
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load environment.properties", e);
        }
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }
}

