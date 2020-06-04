package com.gosea.captain.models;

import com.google.gson.annotations.SerializedName;

public class CheckedStatusResponse {
    @SerializedName("check_in")
    private String check_in;
    @SerializedName("status")
    private boolean status;

    public CheckedStatusResponse() {
    }

    public CheckedStatusResponse(String check_in, boolean status) {
        this.check_in = check_in;
        this.status = status;
    }

    public String getCheck_in() {
        return check_in;
    }

    public void setCheck_in(String check_in) {
        this.check_in = check_in;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
