package com.gosea.captain.models.profile;

import com.google.gson.annotations.SerializedName;

public class ParkingSpot {
    @SerializedName("point")
    private String point;

    @SerializedName("zone")
    private String zone;

    public ParkingSpot() {
    }

    public ParkingSpot(String point, String zone) {
        this.point = point;
        this.zone = zone;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }
}
