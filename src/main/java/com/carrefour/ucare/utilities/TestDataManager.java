package com.carrefour.ucare.utilities;

import io.qameta.allure.internal.shadowed.jackson.databind.JsonNode;
import io.qameta.allure.internal.shadowed.jackson.databind.ObjectMapper;
import java.io.InputStream;

public class TestDataManager {
    private static final JsonNode testData;

    static {
        // Read country/environment from command line (e.g., -Denv=fr), default to "ro"
        String env = System.getProperty("env", "ro").toLowerCase();
        String filePath = "testdata/" + env + ".json";

        try (InputStream is =
                TestDataManager.class.getClassLoader().getResourceAsStream(filePath)) {
            if (is == null) {
                throw new RuntimeException("Test data file not found on classpath: " + filePath);
            }
            ObjectMapper mapper = new ObjectMapper();
            testData = mapper.readTree(is);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load test data for country: " + env, e);
        }
    }

    // Retrieve nested keys using dot notation (e.g., "search.validProductId")
    public static String get(String path) {
        String[] keys = path.split("\\.");
        JsonNode node = testData;
        for (String key : keys) {
            node = node.path(key);
        }
        return node.asText();
    }
}
