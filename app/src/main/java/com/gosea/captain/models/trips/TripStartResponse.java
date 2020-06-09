package com.gosea.captain.models.trips;

import com.google.gson.annotations.SerializedName;

public class TripStartResponse {
    @SerializedName("id")
    private int id;
    @SerializedName("invoice")
    private String invoice;

    public TripStartResponse(int id, String invoice) {
        this.id = id;
        this.invoice = invoice;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getInvoice() {
        return invoice;
    }

    public void setInvoice(String invoice) {
        this.invoice = invoice;
    }
}
