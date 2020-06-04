package com.gosea.captain.models.ticket;

import com.google.gson.annotations.SerializedName;

public class TicketData {
    @SerializedName("id")
    private int id;
    @SerializedName("order")
    private OrderData order;
    @SerializedName("barcode")
    private String barcode;
    @SerializedName("paid")
    private double paid;
    @SerializedName("status")
    private String status;
    @SerializedName("created")
    private String created;

    public TicketData(int id, OrderData order, String barcode, double paid, String status, String created) {
        this.id = id;
        this.order = order;
        this.barcode = barcode;
        this.paid = paid;
        this.status = status;
        this.created = created;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public OrderData getOrder() {
        return order;
    }

    public void setOrder(OrderData order) {
        this.order = order;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public double getPaid() {
        return paid;
    }

    public void setPaid(double paid) {
        this.paid = paid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
