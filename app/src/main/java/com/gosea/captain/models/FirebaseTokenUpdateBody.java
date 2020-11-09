package com.gosea.captain.models;

import com.google.gson.annotations.SerializedName;

public class FirebaseTokenUpdateBody {
    @SerializedName("token")
    private String token;

    public FirebaseTokenUpdateBody(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
