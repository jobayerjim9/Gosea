package com.gosea.captain.models.trips;

import com.google.gson.annotations.SerializedName;

public class TripStartBody {
    @SerializedName("invoice")
    private String invoice;

    public TripStartBody(String invoice) {
        this.invoice = invoice;
    }

    public String getInvoice() {
        return invoice;
    }

    public void setInvoice(String invoice) {
        this.invoice = invoice;
    }
}
