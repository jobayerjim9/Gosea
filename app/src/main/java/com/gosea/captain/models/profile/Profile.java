package com.gosea.captain.models.profile;

import com.google.gson.annotations.SerializedName;
import com.gosea.captain.models.QueueModel;

public class Profile {
    @SerializedName("country")
    private String country;

    @SerializedName("pic")
    private String pic;

    @SerializedName("gender")
    private String gender;

    @SerializedName("dob")
    private String dob;

    @SerializedName("martial")
    private boolean martial;

    @SerializedName("phone")
    private String phone;

    @SerializedName("city")
    private String city;

    @SerializedName("address")
    private String address;

    @SerializedName("education")
    private String education;

    @SerializedName("user_type")
    private String user_type;

    @SerializedName("status")
    private boolean status;

    @SerializedName("q_status")
    private boolean q_status;

    @SerializedName("boat")
    private Boat boat;

    @SerializedName("que")
    private QueueModel que;

    public Profile(String country, String pic, String gender, String dob, boolean martial, String phone, String city, String address, String education, String user_type, boolean status, boolean q_status, Boat boat, QueueModel que) {
        this.country = country;
        this.pic = pic;
        this.gender = gender;
        this.dob = dob;
        this.martial = martial;
        this.phone = phone;
        this.city = city;
        this.address = address;
        this.education = education;
        this.user_type = user_type;
        this.status = status;
        this.q_status = q_status;
        this.boat = boat;
        this.que = que;
    }

    public QueueModel getQue() {
        return que;
    }

    public void setQue(QueueModel que) {
        this.que = que;
    }

    public boolean isQ_status() {
        return q_status;
    }

    public void setQ_status(boolean q_status) {
        this.q_status = q_status;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public boolean isMartial() {
        return martial;
    }

    public void setMartial(boolean martial) {
        this.martial = martial;
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

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Boat getBoat() {
        return boat;
    }

    public void setBoat(Boat boat) {
        this.boat = boat;
    }
}
