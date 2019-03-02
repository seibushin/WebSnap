/* Copyright 2017 Sebastian Meyer (seibushin.de)
 *
 * NO LICENSE
 * YOU MAY NOT REPRODUCE, DISTRIBUTE, OR CREATE DERIVATIVE WORKS FROM MY WORK
 *
 */

package de.seibushin.webSnap.snapper.model;

public class SearchPrice {
    private String search;
    private double price;

    public SearchPrice(String search, double price) {
        this.search = search;
        this.price = price;
    }

    public String getSearch() {
        return search;
    }

    public double getPrice() {
        return price;
    }
}
