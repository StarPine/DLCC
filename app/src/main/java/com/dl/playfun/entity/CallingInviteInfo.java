package com.dl.playfun.entity;

import java.io.Serializable;
import java.util.List;

public class CallingInviteInfo implements Serializable {
    UserProfileInfo userProfileInfo;
    List<String> messages;
    private Integer roomId;
    private Integer userId;
    private Integer minutesRemaining;//剩余通话时长

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public UserProfileInfo getUserProfileInfo() {
        return userProfileInfo;
    }

    public void setUserProfileInfo(UserProfileInfo userProfileInfo) {
        this.userProfileInfo = userProfileInfo;
    }

    public List<String> getMessages() {
        return messages;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }

    public Integer getMinutesRemaining() {
        return minutesRemaining;
    }

    public void setMinutesRemaining(Integer minutesRemaining) {
        this.minutesRemaining = minutesRemaining;
    }
}
