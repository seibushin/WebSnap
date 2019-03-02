/* Copyright 2017 Sebastian Meyer (seibushin.de)
 *
 * NO LICENSE
 * YOU MAY NOT REPRODUCE, DISTRIBUTE, OR CREATE DERIVATIVE WORKS FROM MY WORK
 * 
 */

package de.seibushin.webSnap.helper;

import de.seibushin.webSnap.Log;

import java.util.Properties;

public class PropertiesHelper {
    public static double getDouble(Properties properties, String key, double defaultValue) {
        try {
            double d = Double.parseDouble(properties.getProperty(key));
            return d;
        } catch (NullPointerException e) {
            Log.getInstance().info("Key '" + key + "' not found in properties. Default will be used instead.");
            return defaultValue;
        }
    }

    public static String getString(Properties properties, String key, String defaultValue) {
        String s = properties.getProperty(key);
        if (s == null || s.equals("")) {
            Log.getInstance().info("Key '" + key + "' not found in properties. Default will be used instead.");
            return defaultValue;
        } else {
            return s;
        }
    }

    public static int getInt(Properties properties, String key, int defaultValue) {
        try {
            int i = Integer.parseInt(properties.getProperty(key));
            return i;
        } catch (NumberFormatException | NullPointerException e) {
            Log.getInstance().info("Key '" + key + "' not found in properties. Default will be used instead.");
            return defaultValue;
        }
    }

    public static long getLong(Properties properties, String key, long defaultValue) {
        try {
            long l = Long.parseLong(properties.getProperty(key));
            return l;
        } catch (NullPointerException e) {
            Log.getInstance().info("Key '" + key + "' not found in properties. Default will be used instead.");
            return defaultValue;
        }
    }
}
