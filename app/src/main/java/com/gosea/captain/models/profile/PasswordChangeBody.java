package com.gosea.captain.models.profile;

import com.google.gson.annotations.SerializedName;

public class PasswordChangeBody {
    @SerializedName("password")
    private String password;
    @SerializedName("newpass")
    private String newpass;

    public PasswordChangeBody(String password, String newpass) {
        this.password = password;
        this.newpass = newpass;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNewpass() {
        return newpass;
    }

    public void setNewpass(String newpass) {
        this.newpass = newpass;
    }
}
