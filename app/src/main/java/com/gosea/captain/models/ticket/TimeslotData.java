package com.gosea.captain.models.ticket;

import com.google.gson.annotations.SerializedName;

public class TimeslotData {

    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("duration")
    private int duration;

    public TimeslotData(int id, String name, int duration) {
        this.id = id;
        this.name = name;
        this.duration = duration;
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

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
