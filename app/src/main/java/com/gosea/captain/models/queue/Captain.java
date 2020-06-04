package com.gosea.captain.models.queue;

import com.google.gson.annotations.SerializedName;
import com.gosea.captain.models.profile.Boat;

public class Captain {
    @SerializedName("user")
    private User user;

    @SerializedName("boat")
    private Boat boat;

    public Captain(User user, Boat boat) {
        this.user = user;
        this.boat = boat;
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
