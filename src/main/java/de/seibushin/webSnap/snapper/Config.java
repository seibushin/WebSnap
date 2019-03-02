/* Copyright 2017 Sebastian Meyer (seibushin.de)
 *
 * NO LICENSE
 * YOU MAY NOT REPRODUCE, DISTRIBUTE, OR CREATE DERIVATIVE WORKS FROM MY WORK
 *
 */

package de.seibushin.webSnap.snapper;

import de.seibushin.webSnap.Log;
import de.seibushin.webSnap.helper.PropertiesHelper;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
    private static Config instance;

    private final String configFile = "snap.properties";
    private Properties properties = new Properties();

    private String baseUrl = "";
    private String snapUrl = "";
    private int checkThreads = 1;
    private int snapThreads = 1;
    private int maxRandomWait = 1000;
    private int minRandomWait = 100;
    private int maxTries = 3;
    private String charSet = "UTF-8";

    public Config() {
        try (InputStream input = new FileInputStream(configFile)) {
            properties.load(input);
            loadProperties();
        } catch (IOException e) {
            Log.getInstance().info("No Config file found! Default configuation will be used for all settings!");
        }
    }

    public static Config getInstance() {
        if (Config.instance == null) {
            Config.instance = new Config();
        }
        return Config.instance;
    }

    private void loadProperties() {
        baseUrl = PropertiesHelper.getString(properties, "baseUrl", baseUrl);
        snapUrl = PropertiesHelper.getString(properties, "snapUrl", snapUrl);
        checkThreads = PropertiesHelper.getInt(properties, "checkThreads", checkThreads);
        snapThreads = PropertiesHelper.getInt(properties, "snapThreads", snapThreads);
        maxRandomWait = PropertiesHelper.getInt(properties, "maxRandomWait", maxRandomWait);
        minRandomWait = PropertiesHelper.getInt(properties, "minRandomWait", minRandomWait);
        maxTries = PropertiesHelper.getInt(properties, "maxTries", maxTries);
        charSet = PropertiesHelper.getString(properties, "charSet", charSet);
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public String getSnapUrl() {
        return snapUrl;
    }

    public int getCheckThreads() {
        return checkThreads;
    }

    public int getSnapThreads() {
        return snapThreads;
    }

    public int getMaxRandomWait() {
        return maxRandomWait;
    }

    public int getMinRandomWait() {
        return minRandomWait;
    }

    public int getMaxTries() {
        return maxTries;
    }

    public String getCharSet() {
        return charSet;
    }
}
