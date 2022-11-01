package com.fine.friendlycc.event;

/**
 * Author: 彭石林
 * Time: 2022/1/27 11:34
 * Description: This is GameHeartBeatEvent
 */
public class GameHeartBeatEvent {
    private String doTime;

    public String getDoTime() {
        return doTime;
    }

    public void setDoTime(String doTime) {
        this.doTime = doTime;
    }

    public GameHeartBeatEvent(String doTime) {
        this.doTime = doTime;
    }
}
