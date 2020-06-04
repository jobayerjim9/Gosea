package com.gosea.captain.models;

import com.google.gson.annotations.SerializedName;

public class QueueModel {
    @SerializedName("que_no")
    private String que_no;
    @SerializedName("captain")
    private String captain;
    @SerializedName("boat_t")
    private String boat_t;

    public QueueModel(String que_no, String captain, String boat_t) {
        this.que_no = que_no;
        this.captain = captain;
        this.boat_t = boat_t;
    }

    public String getQue_no() {
        return que_no;
    }

    public void setQue_no(String que_no) {
        this.que_no = que_no;
    }

    public String getCaptain() {
        return captain;
    }

    public void setCaptain(String captain) {
        this.captain = captain;
    }

    public String getBoat_t() {
        return boat_t;
    }

    public void setBoat_t(String boat_t) {
        this.boat_t = boat_t;
    }
}
