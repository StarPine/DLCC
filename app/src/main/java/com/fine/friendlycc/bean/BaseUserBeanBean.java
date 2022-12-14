package com.fine.friendlycc.bean;

import com.google.gson.annotations.SerializedName;

public class BaseUserBeanBean {
    /**
     * id : 5
     * nickname : 广州
     * sex : 0
     * is_vip : 0
     * certification : 0
     */

    private int id;
    private String nickname;
    private Integer sex;
    @SerializedName("is_vip")
    private int isVip;
    private int certification;
    private String avatar;
    private int isAccost;

    public int getIsAccost() {
        return isAccost;
    }

    public void setIsAccost(int isAccost) {
        this.isAccost = isAccost;
    }

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

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public int getIsVip() {
        return isVip;
    }

    public void setIsVip(int isVip) {
        this.isVip = isVip;
    }

    public int getCertification() {
        return certification;
    }

    public void setCertification(int certification) {
        this.certification = certification;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}