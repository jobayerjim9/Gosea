package com.gosea.captain.models.profile;

import com.google.gson.annotations.SerializedName;

public class Boat {

    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("arbicname")
    private String arbicname;

    @SerializedName("boat_typ")
    private BoatType boat_typ;

    @SerializedName("parking_spot")
    private ParkingSpot parking_spot;

    public Boat(int id, String name, String arbicname, BoatType boat_typ, ParkingSpot parking_spot) {
        this.id = id;
        this.name = name;
        this.arbicname = arbicname;
        this.boat_typ = boat_typ;
        this.parking_spot = parking_spot;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArbicname() {
        return arbicname;
    }

    public void setArbicname(String arbicname) {
        this.arbicname = arbicname;
    }

    public BoatType getBoat_typ() {
        return boat_typ;
    }

    public void setBoat_typ(BoatType boat_typ) {
        this.boat_typ = boat_typ;
    }

    public ParkingSpot getParking_spot() {
        return parking_spot;
    }

    public void setParking_spot(ParkingSpot parking_spot) {
        this.parking_spot = parking_spot;
    }
}
