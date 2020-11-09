package com.gosea.captain.models.queue;

import com.google.gson.annotations.SerializedName;
import com.gosea.captain.models.profile.Boat;

public class FixedQueueReponse {
    @SerializedName("id")
    private int id;
    @SerializedName("user")
    private FixedQueueUser user;
    @SerializedName("gender")
    private String gender;
    @SerializedName("phone")
    private String phone;
    @SerializedName("city")
    private String city;
    @SerializedName("address")
    private String address;
    @SerializedName("user_type")
    private String user_type;
    @SerializedName("boat")
    private FixedQueueBoat boat;
    @SerializedName("asign_q")
    private int asign_q;


    public FixedQueueReponse(int id, FixedQueueUser user, String gender, String phone, String city, String address, String user_type, FixedQueueBoat boat, int asign_q) {
        this.id = id;
        this.user = user;
        this.gender = gender;
        this.phone = phone;
        this.city = city;
        this.address = address;
        this.user_type = user_type;
        this.boat = boat;
        this.asign_q = asign_q;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public FixedQueueBoat getBoat() {
        return boat;
    }

    public void setBoat(FixedQueueBoat boat) {
        this.boat = boat;
    }

    public FixedQueueUser getUser() {
        return user;
    }

    public void setUser(FixedQueueUser user) {
        this.user = user;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }


    public int getAsign_q() {
        return asign_q;
    }

    public void setAsign_q(int asign_q) {
        this.asign_q = asign_q;
    }
}
