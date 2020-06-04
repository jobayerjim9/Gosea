package com.gosea.captain.models.ticket;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class TicketResponse {
    @SerializedName("status")
    private int status;
    @SerializedName("Message")
    private String Message;
    @SerializedName("data")
    private ArrayList<TicketData> ticketData;

    public TicketResponse(int status, String message, ArrayList<TicketData> ticketData) {
        this.status = status;
        Message = message;
        this.ticketData = ticketData;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public ArrayList<TicketData> getTicketData() {
        return ticketData;
    }

    public void setTicketData(ArrayList<TicketData> ticketData) {
        this.ticketData = ticketData;
    }
}
