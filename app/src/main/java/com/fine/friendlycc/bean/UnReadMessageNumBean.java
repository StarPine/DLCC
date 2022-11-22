package com.fine.friendlycc.bean;

import com.google.gson.annotations.SerializedName;

public class UnReadMessageNumBean {
    @SerializedName("unread_number")
    private int unreadNumber;

    public int getUnreadNumber() {
        return unreadNumber;
    }

    public void setUnreadNumber(int unreadNumber) {
        this.unreadNumber = unreadNumber;
    }
}