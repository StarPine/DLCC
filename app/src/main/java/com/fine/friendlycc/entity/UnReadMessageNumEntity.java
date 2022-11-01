package com.fine.friendlycc.entity;

import com.google.gson.annotations.SerializedName;

public class UnReadMessageNumEntity {
    @SerializedName("unread_number")
    private int unreadNumber;

    public int getUnreadNumber() {
        return unreadNumber;
    }

    public void setUnreadNumber(int unreadNumber) {
        this.unreadNumber = unreadNumber;
    }
}
