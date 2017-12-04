package org.androidtown.MajorBasicProject2;


public class ItemMarker {

    double lat;
    double lon;
    int price;
    int index;

    public ItemMarker(double lat, double lon, int price, int index) {
        this.lat = lat;
        this.lon = lon;
        this.price = price;
        this.index = index;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}