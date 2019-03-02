/* Copyright 2017 Sebastian Meyer (seibushin.de)
 *
 * NO LICENSE
 * YOU MAY NOT REPRODUCE, DISTRIBUTE, OR CREATE DERIVATIVE WORKS FROM MY WORK
 *
 */

package de.seibushin.webSnap.webSnap;

import de.seibushin.webSnap.snapper.model.CheckObject;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Sample extends Application {
    private Scene scene;
    private Area browser = new Area();

    private WorkQueue workQueue = new WorkQueue(1);

    private void fillMap() {
        workQueue.execute(new SnapTask(browser, new CheckObject("http://google.de", "google")));
        workQueue.execute(new SnapTask(browser, new CheckObject("http://reddit.com/r/leagueoflegends", "reddit")));
        workQueue.execute(new SnapTask(browser, new CheckObject("http://www.golem.de", "golem")));
        workQueue.execute(new SnapTask(browser, new CheckObject("http://www.spiegel.de", "spiegel")));
        workQueue.execute(new SnapTask(browser, new CheckObject("http://heise.de", "heise")));
        workQueue.execute(new SnapTask(browser, new CheckObject("https://amazon.de", "amazonDE")));
        workQueue.execute(new SnapTask(browser, new CheckObject("http://amazon.nl", "amazonNL")));
    }

    @Override
    public void start(Stage stage) {
        // create scene
        stage.setTitle("WebSnapSample");
        VBox parent = new VBox();
        Button snap = new Button("Snap");
        snap.setOnAction(e -> {
            fillMap();
        });
        parent.getChildren().addAll(snap, browser);
        scene = new Scene(parent);
        stage.setScene(scene);
        // show stage
        stage.show();
    }

    public static void main(String[] args){
        launch(args);
    }
}
