package squadra;

import java.io.*;
import java.util.Properties;

public class ConfigManager {
    private Properties properties;
    private String filePath;

    public ConfigManager(String filePath) {
        this.filePath = filePath;
        properties = new Properties();
        loadProperties();
    }

    private void loadProperties() {
        File file = new File(filePath);
        if (file.exists()) {
            try (FileInputStream in = new FileInputStream(file)) {
                properties.load(in);
            } catch (IOException e) {
                System.out.println("Error loading properties file: " + e.getMessage());
            }
        } else {
            System.out.println("Properties file does not exist, will create it on save.");
        }
    }

    public String get(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    public void set(String key, String value, boolean autoSave) {
        properties.setProperty(key, value);
        if (autoSave) save();
    }

    public void save() {
        File file = new File(filePath);

        // Ensure parent directory exists
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }

        // Create file if it doesn't exist
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            try (FileOutputStream out = new FileOutputStream(file)) {
                properties.store(out, "Application Configuration");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}