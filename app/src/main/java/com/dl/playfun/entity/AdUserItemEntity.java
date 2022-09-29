package com.dl.playfun.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Author: 彭石林
 * Time: 2022/7/26 19:09
 * Description: 用户广告位明细
 */
public class AdUserItemEntity {
    private Integer userId;
    private String nickname;
    private String avatar;
    private Integer sex;
    private String sound;
    private String soundTime;
    private String toImId;

    public String getToImId() {
        return toImId;
    }

    public void setToImId(String toImId) {
        this.toImId = toImId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public String getSoundTime() {
        return soundTime;
    }

    public void setSoundTime(String soundTime) {
        this.soundTime = soundTime;
    }
}
