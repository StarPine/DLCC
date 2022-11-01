package com.fine.friendlycc.event;

/**
 * @ClassName UserUpdateVipEvent
 * @Description TODO
 * @Author 彭石林
 * @Date 2021/4/8 17:00
 * @Phone 16620350375
 * @email 15616314565@163.com
 * @Version 1.0
 **/
public class UserUpdateVipEvent {
    private String endTime;
    private int isVip;

    public UserUpdateVipEvent(String endTime, int isVip) {
        this.endTime = endTime;
        this.isVip = isVip;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getIsVip() {
        return isVip;
    }

    public void setIsVip(int isVip) {
        this.isVip = isVip;
    }
}