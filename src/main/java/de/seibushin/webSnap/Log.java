/* Copyright 2017 Sebastian Meyer (seibushin.de)
 *
 * NO LICENSE
 * YOU MAY NOT REPRODUCE, DISTRIBUTE, OR CREATE DERIVATIVE WORKS FROM MY WORK
 *
 */

package de.seibushin.webSnap;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class Log {
    private static Log instance;

    private Stage stage = new Stage();
    private TextArea textArea = new TextArea();
    private Scene scene = new Scene(textArea);

    public Log() {
        textArea.setEditable(false);
        stage.setTitle("AmazonSnap - Logs");

        stage.setScene(scene);
    }

    public static Log getInstance() {
        if (Log.instance == null) {
            Log.instance = new Log();
        }
        return Log.instance;
    }

    public void show() {
        if (stage.isShowing()) {
            stage.requestFocus();
        } else {
            stage.show();
        }
    }

    public void hide() {
        stage.hide();
    }

    public void info(String msg) {
        System.out.println(msg);
        Platform.runLater(() -> {
            textArea.appendText("INFO: " + msg + "\r\n");
        });
    }

    public void error(String msg) {
        System.out.println(msg);
        Platform.runLater(() -> {
            textArea.appendText("ERROR: " + msg + "\r\n");
        });
    }

    public void log(String msg) {
        textArea.appendText(msg);
    }

    public void clear() {
        textArea.clear();
    }
}
