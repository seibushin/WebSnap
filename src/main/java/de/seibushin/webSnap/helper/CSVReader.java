/* Copyright 2017 Sebastian Meyer (seibushin.de)
 *
 * NO LICENSE
 * YOU MAY NOT REPRODUCE, DISTRIBUTE, OR CREATE DERIVATIVE WORKS FROM MY WORK
 *
 */

package de.seibushin.webSnap.helper;

import de.seibushin.webSnap.Log;
import de.seibushin.webSnap.snapper.model.SearchPrice;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CSVReader {
    private static Pattern p = Pattern.compile("(.*?),\\s*(\\d+|\\d+.\\d+)");

    public static List<SearchPrice> read(File file) {
        List<SearchPrice> result = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = "";
            while ((line = br.readLine()) != null) {
                Matcher m = p.matcher(line);

                if (m.matches()) {
                    try {
                        String search = m.group(1);
                        double price = Double.parseDouble(m.group(2));
                        result.add(new SearchPrice(search, price));
                    } catch (NumberFormatException e) {
                        Log.getInstance().info("Invalid line " + line + " - line will not be added to the list!");
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }
}
