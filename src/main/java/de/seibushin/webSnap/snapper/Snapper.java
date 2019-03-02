/* Copyright 2017 Sebastian Meyer (seibushin.de)
 *
 * NO LICENSE
 * YOU MAY NOT REPRODUCE, DISTRIBUTE, OR CREATE DERIVATIVE WORKS FROM MY WORK
 *
 */

package de.seibushin.webSnap.snapper;

import de.seibushin.webSnap.Log;
import de.seibushin.webSnap.snapper.model.CheckObject;
import de.seibushin.webSnap.snapper.model.SearchPrice;
import de.seibushin.webSnap.snapper.model.SnapObject;
import de.seibushin.webSnap.licenseCheck.Trial;
import de.seibushin.webSnap.snapper.model.UserAgents;
import de.seibushin.webSnap.webSnap.Area;
import de.seibushin.webSnap.webSnap.SnapTask;
import de.seibushin.webSnap.webSnap.WorkQueue;
import javafx.beans.property.IntegerProperty;
import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.*;
import java.net.SocketTimeoutException;
import java.text.NumberFormat;

public class Snapper {
    private Trial trial = new Trial();
    private Area webSnapArea = new Area();

    private WorkQueue snapQueue = new WorkQueue(Config.getInstance().getSnapThreads());
    private WorkQueue checkQueue = new WorkQueue(Config.getInstance().getCheckThreads());

    private int maxTries = Config.getInstance().getMaxTries();
    private int minRandomWait = Config.getInstance().getMinRandomWait();
    private int maxRandomWait = Config.getInstance().getMaxRandomWait();
    private String charSet = Config.getInstance().getCharSet();

    private NumberFormat nf = NumberFormat.getInstance();
    private File dir = new File("snapresults");
    private File output = new File(dir, "output.csv");

    public Snapper() {

    }

    public IntegerProperty doneSnapsProperty() {
        return snapQueue.doneProperty();
    }

    public void checkLicense() {
        trial.show();
    }

    public IntegerProperty queuedSnapsProperty() {
        return snapQueue.queuedProperty();
    }

    public IntegerProperty doneChecksProperty() {
        return checkQueue.doneProperty();
    }

    public IntegerProperty queuedChecksProperty() {
        return checkQueue.queuedProperty();
    }

    public Area getWebSnapArea() {
        return webSnapArea;
    }

    private SnapObject snapObject;

    private void reset() {
        snapQueue.reset();
        checkQueue.reset();
    }

    public void start(SnapObject snapObject) {
        if (trial.isValid()) {
            Log.getInstance().info("start snapping");
            webSnapArea.init();
            reset();
            writeHeadline();
            this.snapObject = snapObject;

            if (this.snapObject.getValues() != null) {
                snapObject.getValues().stream().limit(trial.getSnapLimit()).forEach(searchPrice -> {
                    checkQueue.execute(new CheckTask(searchPrice));
                });
            }
        } else {
            Log.getInstance().error("Invalid license. Cant snap!");
        }
    }

    private void writeHeadline() {
        dir.mkdir();
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(output, false), charSet))) {
            writer.write(CheckObject.getCSVHeadline());
            writer.flush();
        } catch (IOException e) {
            Log.getInstance().error("CSV headline could not be written!");
        }
    }

    private void writeOutput(String s) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(output, true), charSet))) {
            writer.write(s);
            writer.flush();
        }
    }

    private Document connect(String url) {
        Document doc = null;
        int tries = 1;

        while (tries <= maxTries) {
            //@todo better handling of timeouts
            try {
                // simulate a random wait between minWait and (minWait + maxWait)
                int wait = (int) ((Math.random() * maxRandomWait) + minRandomWait);
                Thread.sleep(wait);

                Connection.Response response = Jsoup.connect(url)
                        .ignoreContentType(true)
                        .ignoreHttpErrors(true)
                        .timeout(10000)
                        // use random UserAgent to prevent beeing detected as a bot
                        .userAgent(UserAgents.getInstance().getRandomUserAgent())
                        .execute();

                if (response.statusCode() == 200) {
                    Log.getInstance().info(response.statusCode() + ": " + url);
                    doc = response.parse();
                    // stop the loop if successful
                    tries = maxTries + 1;
                } else {
                    tries++;
                    if (tries > maxTries) {
                        Log.getInstance().error(response.statusMessage());
                        Log.getInstance().error("cant reach: " + url);
                    }
                }
            } catch (SocketTimeoutException | HttpStatusException e) {
                tries++;
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                Log.getInstance().error("Error while sleeping...");
            }
        }

        return doc;
    }

    // inner class
    public class CheckTask implements Runnable {
        private SearchPrice searchPrice;

        public CheckTask(SearchPrice searchPrice) {
            this.searchPrice = searchPrice;
        }

        @Override
        public void run() {
            Log.getInstance().info("check " + searchPrice.getSearch());
            Document doc = null;
            try {
                CheckObject checkObject = new CheckObject();
                checkObject.setSearch(searchPrice.getSearch());
                checkObject.setPrice(searchPrice.getPrice());

                try {
                    doc = connect(snapObject.getBaseUrl() + searchPrice.getSearch());

                    // get asin to access seller page
                    checkObject.setAsin(doc.select("li.s-result-item").first().attr("data-asin"));

                    // access seller page
                    doc = connect(snapObject.getSnapUrl() + checkObject.getAsin());

                    // get cheapest price on seller page
                    checkObject.setAmazonprice(nf.parse(doc.select("span.olpOfferPrice").first().text().replaceAll(".*?(\\d+[,.]\\d+).*", "$1")).doubleValue());
                    checkObject.setShipping(0);
                    try {
                        checkObject.setShipping(nf.parse(doc.select(".olpOffer").first().select(".olpShippingPrice").first().text().replaceAll(".*?(\\d+[,.]\\d+).*", "$1")).doubleValue());
                    } catch (Exception ex) {
                    }

                    checkObject.setSeller(doc.select(".olpSellerName").first().text());
                    checkObject.setSnapUrl(snapObject.getSnapUrl() + checkObject.getAsin());

                    if (checkObject.getAmazonprice() < checkObject.getPrice()) {
                        // snap it
                        checkObject.setSnapName();
                        snapQueue.execute(new SnapTask(webSnapArea, checkObject));
                    }
                } catch (Exception e) {
                    checkObject.setAsin("not listed");
                }

                writeOutput(checkObject.toCSVString());
                Log.getInstance().info("checked " + checkObject.getSearch());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
