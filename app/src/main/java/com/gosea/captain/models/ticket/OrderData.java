package com.gosea.captain.models.ticket;

import com.google.gson.annotations.SerializedName;
import com.gosea.captain.models.profile.BoatType;

public class OrderData {

    @SerializedName("name")
    private String name;

    @SerializedName("phone")
    private String phone;

    @SerializedName("peoples")
    private int peoples;

    @SerializedName("timeslot")
    private TimeslotData timeslot;

    @SerializedName("boat")
    private BoatType boat;


    public OrderData(String name, String phone, int peoples, TimeslotData timeslot, BoatType boat) {
        this.name = name;
        this.phone = phone;
        this.peoples = peoples;
        this.timeslot = timeslot;
        this.boat = boat;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getPeoples() {
        return peoples;
    }

    public void setPeoples(int peoples) {
        this.peoples = peoples;
    }

    public TimeslotData getTimeslot() {
        return timeslot;
    }

    public void setTimeslot(TimeslotData timeslot) {
        this.timeslot = timeslot;
    }

    public BoatType getBoat() {
        return boat;
    }

    public void setBoat(BoatType boat) {
        this.boat = boat;
    }
}
