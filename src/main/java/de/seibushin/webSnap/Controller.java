/* Copyright 2017 Sebastian Meyer (seibushin.de)
 *
 * NO LICENSE
 * YOU MAY NOT REPRODUCE, DISTRIBUTE, OR CREATE DERIVATIVE WORKS FROM MY WORK
 *
 */

package de.seibushin.webSnap;

import de.seibushin.webSnap.snapper.model.SnapObject;
import de.seibushin.webSnap.snapper.Snapper;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class Controller {
    @FXML
    VBox container;
    @FXML
    ProgressBar snapProgress;
    @FXML
    ProgressBar checkProgress;
    @FXML
    Label snaps;
    @FXML
    Label checks;
    @FXML
    Menu recentFiles;
    @FXML
    Button start;

    Snapper snapper = new Snapper();
    SnapObject snapObject = new SnapObject();

    private void checkDone(int newValue) {
        if (newValue > 0 && snapper.doneSnapsProperty().get() >= snapper.queuedSnapsProperty().get() && snapper.doneChecksProperty().get() >= snapper.queuedChecksProperty().get()) {
            Log.getInstance().info("done snapping");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("AmazonSnap");
            alert.setHeaderText(null);
            alert.setGraphic(null);
            alert.setContentText("Done snapping!");
            alert.showAndWait();
            start.setDisable(false);
        }
    }

    @FXML
    private void initialize() {
        snapper.checkLicense();
        container.getChildren().add(snapper.getWebSnapArea());

        snapper.doneSnapsProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                Platform.runLater(() -> {
                    snaps.setText("(" + newValue.toString() + "/" + snapper.queuedSnapsProperty().get() + ") Snaps!");

                    checkDone(newValue.intValue());
                });
            }
        });

        snapper.queuedSnapsProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                //progressBar.setProgress(newValue.doubleValue());
                Platform.runLater(() -> {
                    snaps.setText("(" + snapper.doneSnapsProperty().get() + "/" + newValue.toString() + ") Snaps!");
                });
            }
        });

        snapper.doneChecksProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                Platform.runLater(() -> {
                    checks.setText("(" + newValue.toString() + "/" + snapper.queuedChecksProperty().get() + ") Checks!");

                    checkDone(newValue.intValue());
                });
            }
        });

        snapper.queuedChecksProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                Platform.runLater(() -> {
                    checks.setText("(" + snapper.doneChecksProperty().get() + "/" + newValue.toString() + ") Checks!");
                });
            }
        });
    }

    @FXML
    private void selectFile() {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showOpenDialog(new Stage());
        snapObject.setInput(file);
    }

    @FXML
    public void start() {
        // disable Button
        start.setDisable(true);

        if (snapObject.getInput() == null) {
            selectFile();
            if (snapObject.getInput() != null) {
                start();
            } else {
                // enable Button
                start.setDisable(false);
            }
        } else {
            snapper.start(snapObject);
        }
    }

    @FXML
    public void createOutput(ActionEvent actionEvent) {
        // set value of createOutput to the value of the checkmenuitem
        snapObject.setCreateOutput(((CheckMenuItem) actionEvent.getSource()).isSelected());
    }

    public void showLog(ActionEvent actionEvent) {
        Log.getInstance().show();
    }
}
