package com.gosea.captain.models;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("data")
    private String token;
    @SerializedName("status")
    private String status;

    public LoginResponse() {
    }

    public LoginResponse(String token, String status) {
        this.token = token;
        this.status = status;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
