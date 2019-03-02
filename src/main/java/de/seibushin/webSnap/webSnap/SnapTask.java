/* Copyright 2017 Sebastian Meyer (seibushin.de)
 *
 * NO LICENSE
 * YOU MAY NOT REPRODUCE, DISTRIBUTE, OR CREATE DERIVATIVE WORKS FROM MY WORK
 * 
 */

package de.seibushin.webSnap.webSnap;

import de.seibushin.webSnap.snapper.model.CheckObject;
import de.seibushin.webSnap.Log;

public class SnapTask implements Runnable {
    private Area browser;
    private CheckObject checkObject;

    public SnapTask(Area browser, CheckObject checkObject) {
        this.browser = browser;
        this.checkObject = checkObject;
    }

    @Override
    public void run() {
        Log.getInstance().info("snap " + checkObject.getSnapUrl());
        browser.snap(checkObject);
    }
}
