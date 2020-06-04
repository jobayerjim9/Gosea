package com.gosea.captain.models;

import com.google.gson.annotations.SerializedName;

public class CheckOutStatus {
    @SerializedName("check_out")
    private String check_out;
    @SerializedName("status")
    private boolean status;

    public CheckOutStatus(String check_out, boolean status) {
        this.check_out = check_out;
        this.status = status;
    }

    public String getCheck_out() {
        return check_out;
    }

    public void setCheck_out(String check_out) {
        this.check_out = check_out;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
