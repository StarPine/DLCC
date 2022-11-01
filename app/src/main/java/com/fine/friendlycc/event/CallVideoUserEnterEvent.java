package com.fine.friendlycc.event;

/**
 * Author: 彭石林
 * Time: 2021/12/18 10:27
 * Description: This is CallVideoUserEnter
 */
public class CallVideoUserEnterEvent {
    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public CallVideoUserEnterEvent(String userId) {
        this.userId = userId;
    }

    public CallVideoUserEnterEvent() {
    }
}
