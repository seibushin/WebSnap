/* Copyright 2017 Sebastian Meyer (seibushin.de)
 *
 * NO LICENSE
 * YOU MAY NOT REPRODUCE, DISTRIBUTE, OR CREATE DERIVATIVE WORKS FROM MY WORK
 * 
 */

package de.seibushin.webSnap.webSnap;

import de.seibushin.webSnap.snapper.model.CheckObject;
import de.seibushin.webSnap.Log;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.concurrent.Worker;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

public class Area extends Region {
    private WebView webView = new WebView();
    private WebEngine webEngine = webView.getEngine();
    private Label desc = new Label(LocalDate.now().toString());

    private VBox snapContainer = new VBox(desc, webView);

    private ScrollPane scrollPane = new ScrollPane(snapContainer);

    private BooleanProperty ready = new SimpleBooleanProperty(true);
    private Config config = new Config();

    private CheckObject checkObject;
    private long minSnapSize;

    //@todo get from Snapper Object
    private File dir = new File("snapresults");

    public Area() {
        webView.setMaxHeight(config.getWebViewMaxHeight());
        webView.setMinHeight(config.getWebViewMinHeight());
        webView.setMaxWidth(config.getWebViewMaxWidth());
        webView.setMinWidth(config.getWebViewMinWidth());
        webView.setZoom(config.getWebViewZoom());

        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setMaxHeight(config.getScrollPaneMaxHeight());
        scrollPane.setMinHeight(config.getScrollPaneMinHeight());
        scrollPane.setMaxWidth(config.getScrollPaneMaxWidth());
        scrollPane.setMinWidth(config.getScrollPaneMinWidth());

        webEngine.getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == Worker.State.SUCCEEDED) {
                if (webEngine.getDocument().getDocumentURI() != null && !"about:blank".equals(webEngine.getDocument().getDocumentURI())) {
                    snap();
                } else {
                    Log.getInstance().info("cleaned and ready again!");
                    ready.setValue(true);
                }
            }
        });

        getChildren().add(scrollPane);
    }

    private void determineEmptySnapSize() {
        try {
            Image image = webView.snapshot(null, null);
            BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
            dir.mkdir();
            File snap = new File(dir, config.getWebViewMaxWidth() + "x" + config.getWebViewMaxHeight() + "." + CheckObject.getSnapExtension());
            ImageIO.write(bufferedImage, CheckObject.getSnapExtension(), snap);
            minSnapSize = snap.length();
            snap.delete();
            Log.getInstance().info("minSnapSize is " + minSnapSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int maxTries = 3;
    private int tries = 0;
    private long lastSnapSize = 0;

    private void snap() {
        if (tries < maxTries) {
            try {
                // todo bad performance floods the ram
                Thread.sleep(config.getSleep());
                Image image = webView.snapshot(null, null);
                BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
                dir.mkdir();
                File snap = new File(dir, "_" + checkObject.getSnapName());
                ImageIO.write(bufferedImage, checkObject.getSnapExtension(), snap);
                long snapSize = snap.length();
                snap.delete();
                if (snapSize > (minSnapSize*2)) {
                    if (snapSize != lastSnapSize) {
                        lastSnapSize = snapSize;
                        desc.setText(LocalDate.now().toString() + " | " + checkObject.getSearch() + " | " + checkObject.getAmazonprice() + " < " + checkObject.getPrice());
                        image = snapContainer.snapshot(null, null);
                        bufferedImage = SwingFXUtils.fromFXImage(image, null);
                        dir.mkdir();
                        snap = new File(dir, checkObject.getSnapName());
                        ImageIO.write(bufferedImage, checkObject.getSnapExtension(), snap);

                        Log.getInstance().info("snapped " + checkObject.getSnapName() + " --> " + snap.length());
                        webEngine.loadContent("");
                    } else {
                        Log.getInstance().info("not snapped - same size as previous snap! " + checkObject.getSnapName());
                        Platform.runLater(() -> {
                            webEngine.load(checkObject.getSnapUrl());
                        });
                    }
                } else {
                    Platform.runLater(() -> {
                        webEngine.load(checkObject.getSnapUrl());
                    });
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            tries++;
        } else {
            Log.getInstance().error("not snapped " + checkObject.getSearch());
            webEngine.loadContent("");
        }
    }

    public void snap(CheckObject checkObject) {
        try {
            tries = 0;
            ready.setValue(false);
            //@todo prevent double snap (snapping the last used url)
            this.checkObject = checkObject;
            Platform.runLater(() -> {
                webEngine.load(this.checkObject.getSnapUrl());
            });

            // wait for the Area to be ready again
            while (!ready.get()) {
                // wait do nothing...
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void init() {
        determineEmptySnapSize();
    }
}
