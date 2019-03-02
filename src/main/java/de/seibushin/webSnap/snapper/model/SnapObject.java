/* Copyright 2017 Sebastian Meyer (seibushin.de)
 *
 * NO LICENSE
 * YOU MAY NOT REPRODUCE, DISTRIBUTE, OR CREATE DERIVATIVE WORKS FROM MY WORK
 *
 */

package de.seibushin.webSnap.snapper.model;

import de.seibushin.webSnap.helper.CSVReader;
import de.seibushin.webSnap.snapper.Config;

import java.io.File;
import java.util.List;

public class SnapObject {
    private boolean createOutput = false;
    private File input;
    private File output;

    private List<SearchPrice> values;

    private String baseUrl = Config.getInstance().getBaseUrl();
    private String snapUrl = Config.getInstance().getSnapUrl();

    public String getBaseUrl() {
        return baseUrl;
    }

    public String getSnapUrl() {
        return snapUrl;
    }

    public void setCreateOutput(boolean createOutput) {
        this.createOutput = createOutput;
    }

    public boolean isCreateOutput() {
        return createOutput;
    }

    public void setInput(File input) {
        this.input = input;

        if (input != null) {
            values = CSVReader.read(input);
        }
    }

    public File getInput() {
        return input;
    }

    public List<SearchPrice> getValues() {
        return values;
    }
}
