package com.fine.friendlycc.bean;

import java.io.Serializable;

/**
 * Author: 彭石林
 * Time: 2022/9/1 17:11
 * Description: This is CoinPusherRoomDeviceInfo
 */
public class CoinPusherRoomDeviceInfo implements Serializable {
    private int id;
    //房间名
    private String nickname;
    //图标
    private String icon;
    //房间ID
    private int roomId;
    //所需金币
    private int money;
    //等级
    private int levelId;
    //房间状态 0:空闲 1:热玩中
    private int status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getLevelId() {
        return levelId;
    }

    public void setLevelId(int levelId) {
        this.levelId = levelId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
