package com.example.proiect;

public class VideoFeed {
    public String nume;
    public String zona;
    public String status;
    public double lat;
    public double lng;
    public String url;

    public VideoFeed(String nume, String zona, String status, double lat, double lng, String url) {
        this.nume = nume;
        this.zona = zona;
        this.status = status;
        this.lat = lat;
        this.lng = lng;
        this.url = url;
    }
}
