package com.gosea.captain.models.profile;

import com.google.gson.annotations.SerializedName;

public class ProfileResponse {
    @SerializedName("first_name")
    private String first_name;

    @SerializedName("last_name")
    private String last_name;

    @SerializedName("username")
    private String username;

    @SerializedName("email")
    private String email;

    public ProfileResponse(String first_name, String last_name, String username, String email) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.username = username;
        this.email = email;
    }
    @SerializedName("profile")
    private Profile profile;


    public ProfileResponse(String first_name, String last_name, String username, String email, Profile profile) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.username = username;
        this.email = email;
        this.profile = profile;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }
}
