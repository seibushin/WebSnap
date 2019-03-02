/* Copyright 2017 Sebastian Meyer (seibushin.de)
 *
 * NO LICENSE
 * YOU MAY NOT REPRODUCE, DISTRIBUTE, OR CREATE DERIVATIVE WORKS FROM MY WORK
 * 
 */

package de.seibushin.webSnap.webSnap;

import de.seibushin.webSnap.Log;
import de.seibushin.webSnap.helper.PropertiesHelper;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
    Properties properties = new Properties();

    private final String configFile = "webSnap.properties";

    private double webViewMaxHeight = 1080;
    private double webViewMinHeight = 1080;
    private double webViewMaxWidth = 1920;
    private double webViewMinWidth = 1920;
    private double webViewZoom = 1;
    private double scrollPaneMaxHeight = 0;
    private double scrollPaneMinHeight = 0;
    private double scrollPaneMaxWidth = 0;
    private double scrollPaneMinWidth = 0;
    private long sleep = 0;

    public Config() {
        try (InputStream input = new FileInputStream(configFile)) {
            properties.load(input);
            loadProperties();
        } catch (IOException e) {
            Log.getInstance().info("No Config file found! Default configuation will be used for all settings!");
        }
    }

    private void loadProperties() {
        webViewMaxHeight = PropertiesHelper.getDouble(properties, "webView.maxHeight", webViewMaxHeight);
        webViewMinHeight = PropertiesHelper.getDouble(properties, "webView.minHeight", webViewMinHeight);
        webViewMaxWidth = PropertiesHelper.getDouble(properties, "webView.maxWidth", webViewMaxWidth);
        webViewMinWidth = PropertiesHelper.getDouble(properties, "webView.minWidth", webViewMinWidth);
        webViewZoom = PropertiesHelper.getDouble(properties, "webView.zoom", webViewZoom);
        scrollPaneMaxHeight = PropertiesHelper.getDouble(properties, "scrollPane.maxHeight", scrollPaneMaxHeight);
        scrollPaneMinHeight = PropertiesHelper.getDouble(properties, "scrollPane.minHeight", scrollPaneMinHeight);
        scrollPaneMaxWidth = PropertiesHelper.getDouble(properties, "scrollPane.maxWidth", scrollPaneMaxWidth);
        scrollPaneMinWidth = PropertiesHelper.getDouble(properties, "scrollPane.minWidth", scrollPaneMinWidth);
        sleep = PropertiesHelper.getLong(properties, "sleep", sleep);
    }

    public double getWebViewMaxHeight() {
        return webViewMaxHeight;
    }

    public double getWebViewMinHeight() {
        return webViewMinHeight;
    }

    public double getWebViewMaxWidth() {
        return webViewMaxWidth;
    }

    public double getWebViewMinWidth() {
        return webViewMinWidth;
    }

    public double getWebViewZoom() {
        return webViewZoom;
    }

    public double getScrollPaneMaxHeight() {
        return scrollPaneMaxHeight;
    }

    public double getScrollPaneMinHeight() {
        return scrollPaneMinHeight;
    }

    public double getScrollPaneMaxWidth() {
        return scrollPaneMaxWidth;
    }

    public double getScrollPaneMinWidth() {
        return scrollPaneMinWidth;
    }

    public long getSleep() {
        return sleep;
    }
}
