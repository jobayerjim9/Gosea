package com.gosea.captain.models;

import com.google.gson.annotations.SerializedName;

public class BasicResponse {
    @SerializedName("status")
    private int status;

    @SerializedName("message")
    private String message;

    public BasicResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
