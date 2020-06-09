package com.gosea.captain.models.trips;

import com.google.gson.annotations.SerializedName;

public class TripTimeRemainingResponse {
    @SerializedName("status")
    private int status;
    @SerializedName("minutes")
    private int minutes;
    @SerializedName("seconds")
    private int seconds;
    @SerializedName("Message")
    private String Message;


    public TripTimeRemainingResponse(int status, int minutes, int seconds, String Message) {
        this.status = status;
        this.minutes = minutes;
        this.seconds = seconds;
        this.Message = Message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }
}
