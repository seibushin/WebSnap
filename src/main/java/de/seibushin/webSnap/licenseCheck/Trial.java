/* Copyright 2017 Sebastian Meyer (seibushin.de)
 *
 * NO LICENSE
 * YOU MAY NOT REPRODUCE, DISTRIBUTE, OR CREATE DERIVATIVE WORKS FROM MY WORK
 *
 */

package de.seibushin.webSnap.licenseCheck;

import de.seibushin.webSnap.Log;
import de.seibushin.webSnap.licenseCheck.model.License;
import javafx.scene.control.Alert;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDate;
import java.util.Scanner;

public class Trial {
    private File licenseFile = new File("license.as");
    private License license;

    public Trial() {
    }

    private boolean check() {
        try (Scanner sc = new Scanner(licenseFile);
             InputStream is = new URL("https://seibushin.de/licenses.as").openStream()) {
            String localLicense = sc.nextLine();
            Scanner scanner = new Scanner(is).useDelimiter("\r\n");
            String s;
            while (scanner.hasNext()) {
                s = scanner.next();
                if (s.matches("^" + localLicense + " .*")) {
                    System.out.println("License found");
                    license = new License(s);
                    return checkExpiration();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean checkExpiration() {
        if (license.getDate().isAfter(LocalDate.now())) {
            return true;
        }
        msg = "License expired! Programm will be terminated...";
        return false;
    }

    public int getSnapLimit() {
        return license.getSnapLimit();
    }

    private String msg = "No valid license! Programm will be terminated...";

    private boolean valid = false;

    public boolean isValid() {
        return valid;
    }

    public void show() {
        valid = check();
        if (valid) {
            Log.getInstance().info("Valid license");
            msg = "Valid license!\r\n" + license.toString();
        } else {
            Log.getInstance().error("No valid license");
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("AmazonSnap");
        alert.setHeaderText(null);
        alert.setGraphic(null);
        alert.setContentText(msg);
        alert.setOnCloseRequest(event -> {
            if (!valid) {
                System.exit(0);
            }
        });
        alert.showAndWait();
    }
}