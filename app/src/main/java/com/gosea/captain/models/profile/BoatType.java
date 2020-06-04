package com.gosea.captain.models.profile;

import com.google.gson.annotations.SerializedName;

public class BoatType {
    @SerializedName("id")
    private int id;
    @SerializedName("b_type")
    private String b_type;
    @SerializedName("max_cap")
    private int max_cap;
    @SerializedName("min_cap")
    private int min_cap;

    public BoatType(int id, String b_type, int max_cap, int min_cap) {
        this.id = id;
        this.b_type = b_type;
        this.max_cap = max_cap;
        this.min_cap = min_cap;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getB_type() {
        return b_type;
    }

    public void setB_type(String b_type) {
        this.b_type = b_type;
    }

    public int getMax_cap() {
        return max_cap;
    }

    public void setMax_cap(int max_cap) {
        this.max_cap = max_cap;
    }

    public int getMin_cap() {
        return min_cap;
    }

    public void setMin_cap(int min_cap) {
        this.min_cap = min_cap;
    }
}
