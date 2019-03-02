/* Copyright 2017 Sebastian Meyer (seibushin.de)
 *
 * NO LICENSE
 * YOU MAY NOT REPRODUCE, DISTRIBUTE, OR CREATE DERIVATIVE WORKS FROM MY WORK
 *
 */

package de.seibushin.webSnap.licenseCheck.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import static java.time.temporal.ChronoUnit.DAYS;

public class License {
    private String type;
    private String customer;
    private LocalDate date;
    private String license;
    private int snapLimit;

    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public License(String s) {
        this.license = s;
        parseLicense(s);
    }

    private void parseLicense(String s) {
        Scanner scanner = new Scanner(s).useDelimiter(" ");
        customer = scanner.next();
        type = scanner.next();

        date = LocalDate.parse(scanner.next(), dtf);
        snapLimit = scanner.nextInt();
        scanner.close();
    }

    public int getSnapLimit() {
        return 1000;
        // todo change
        //return snapLimit;
    }

    public String getType() {
        return type;
    }

    public String getCustomer() {
        return customer;
    }

    public LocalDate getDate() {
        return date;
    }

    private String getRemainingDays() {
        long remains = DAYS.between(LocalDate.now(), date);
        return remains + (remains > 1 ? " days" : " day") + " remaining!";
    }

    public String getLicense() {
        return license;
    }

    public String toString() {
        return "Customer: " + customer + "\r\n" +
                "Type: " + type + " (" + snapLimit + " snaps per run)\r\n" +
                "Expiration date: " + dtf.format(date) + "\r\n" +
                getRemainingDays();
    }
}
