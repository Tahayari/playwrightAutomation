package ro.carrefour.ucare.utilities;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * ConfigManager loads and provides access to application configuration properties from the
 * environment.properties file
 */
public class ConfigManager {

    private static ConfigManager instance;
    private final Properties properties;

    private ConfigManager() {
        // -Denv=ro  or  -Denv=be  — defaults to ro if not supplied
        String env = System.getProperty("env", "ro");
        String configFile = "config/" + env + ".properties";

        try (InputStream input = getClass().getClassLoader().getResourceAsStream(configFile)) {

            if (input == null) {
                throw new RuntimeException(
                        "Config file not found on classpath: "
                                + configFile
                                + " — did you pass -Denv correctly?");
            }
            properties = new Properties();
            properties.load(input);

        } catch (IOException e) {
            throw new RuntimeException("Failed to load config file: " + configFile, e);
        }
    }

    public static synchronized ConfigManager getInstance() {
        if (instance == null) {
            instance = new ConfigManager();
        }
        return instance;
    }

    /**
     * System property always wins over the file value. This lets CI/CD inject credentials without
     * modifying any file: mvn test -Denv=ro -Dapp.username=$SECRET_USER -Dapp.password=$SECRET_PASS
     */
    public String getProperty(String key) {
        String value = System.getProperty(key, properties.getProperty(key));
        if (value == null || value.isBlank()) {
            throw new RuntimeException(
                    "Required property '"
                            + key
                            + "' not found in system properties "
                            + "or config/[env].properties");
        }
        return value;
    }

    public String getProperty(String key, String defaultValue) {
        return System.getProperty(key, properties.getProperty(key, defaultValue));
    }
}
