package com.gosea.captain.models.queue;

import com.google.gson.annotations.SerializedName;
import com.gosea.captain.models.profile.Boat;

public class Captain {
    @SerializedName("user")
    private User user;

    @SerializedName("boat")
    private Boat boat;

    @SerializedName("left_q")
    private int left_q;

    public Captain(User user, Boat boat, int left_q) {
        this.user = user;
        this.boat = boat;
        this.left_q = left_q;
    }

    public int getLeft_q() {
        return left_q;
    }

    public void setLeft_q(int left_q) {
        this.left_q = left_q;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Boat getBoat() {
        return boat;
    }

    public void setBoat(Boat boat) {
        this.boat = boat;
    }
}
