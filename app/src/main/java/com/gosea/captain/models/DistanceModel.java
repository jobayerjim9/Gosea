package com.gosea.captain.models;

import com.google.gson.annotations.SerializedName;

public class DistanceModel {
    @SerializedName("id")
    private int id;
    @SerializedName("lat")
    private double lat;
    @SerializedName("lon")
    private double lon;
    @SerializedName("radius")
    private int radius;

    public DistanceModel(int id, double lat, double lon, int radius) {
        this.id = id;
        this.lat = lat;
        this.lon = lon;
        this.radius = radius;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }
}

