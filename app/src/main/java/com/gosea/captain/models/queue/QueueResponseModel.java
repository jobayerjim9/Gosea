package com.gosea.captain.models.queue;

import com.google.gson.annotations.SerializedName;

public class QueueResponseModel {
    @SerializedName("que_no")
    private int que_no;
    @SerializedName("captain")
    private Captain captain;

    public QueueResponseModel(int que_no, Captain captain) {
        this.que_no = que_no;
        this.captain = captain;
    }

    public int getQue_no() {
        return que_no;
    }

    public void setQue_no(int que_no) {
        this.que_no = que_no;
    }

    public Captain getCaptain() {
        return captain;
    }

    public void setCaptain(Captain captain) {
        this.captain = captain;
    }
}
