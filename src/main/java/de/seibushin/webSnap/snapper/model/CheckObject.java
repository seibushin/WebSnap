/* Copyright 2017 Sebastian Meyer (seibushin.de)
 *
 * NO LICENSE
 * YOU MAY NOT REPRODUCE, DISTRIBUTE, OR CREATE DERIVATIVE WORKS FROM MY WORK
 *
 */

package de.seibushin.webSnap.snapper.model;

public class CheckObject {
    private String search;
    private String asin;
    private double price;
    private double amazonprice;
    private double shipping;
    private String seller;
    private static String snapExtension = "png";
    private String snapUrl;
    private String snapName = "";

    public CheckObject() {
        // default constructor
    }

    public CheckObject(String snapUrl, String search) {
        this.search = search;
        this.snapUrl = snapUrl;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getAsin() {
        return asin;
    }

    public void setAsin(String asin) {
        this.asin = asin;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getAmazonprice() {
        return amazonprice;
    }

    public void setAmazonprice(double amazonprice) {
        this.amazonprice = amazonprice;
    }

    public double getShipping() {
        return shipping;
    }

    public void setShipping(double shipping) {
        this.shipping = shipping;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public void setSnapName() {
        snapName = format(search) + "_" + format(seller) + "." + snapExtension;
    }

    public String getSnapName() {
        return snapName;
    }

    public static String getSnapExtension() {
        return snapExtension;
    }

    public void setSnapUrl(String snapUrl) {
        this.snapUrl = snapUrl;
    }

    public String getSnapUrl() {
        return snapUrl;
    }

    private String format(String s) {
        try {
            return s.replaceAll(",", "");
        } catch (NullPointerException e) {
            return "";
        }
    }

    public static String getCSVHeadline() {
        return "search,asin,price(MSRP),amazonprice,shipping,seller,snapname\r\n";
    }

    public String toCSVString() {
        return format(search) + "," + format(asin) + "," + price + "," + amazonprice + "," + shipping + "," + format(seller) + "," + snapName + "\r\n";
    }
}
