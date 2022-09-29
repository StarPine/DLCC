package com.dl.playfun.event;

/**
 * @author wulei
 */
public class UserRemarkChangeEvent {

    private int userId;
    private String remarkName;

    public UserRemarkChangeEvent(int userId, String remarkName) {
        this.userId = userId;
        this.remarkName = remarkName;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getRemarkName() {
        return remarkName;
    }

    public void setRemarkName(String remarkName) {
        this.remarkName = remarkName;
    }
}
