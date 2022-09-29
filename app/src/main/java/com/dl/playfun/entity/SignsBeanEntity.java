package com.dl.playfun.entity;

import com.google.gson.annotations.SerializedName;

public class SignsBeanEntity {
    /**
     * id : 5
     * nickname : 小四
     * sex : 1
     * avatar : null
     * is_vip : 0
     * img : images/微信图片_20200419083113.png
     * created_at : 2020-05-07 15:45:00
     */


    private int id;
    private String nickname;
    private int sex;
    private Object avatar;
    @SerializedName("is_vip")
    private int isVip;
    private String img;
    @SerializedName("created_at")
    private String createdAt;
    private Integer certification;
    @SerializedName("user_id")
    private Integer userId;

    public Integer getCertification() {
        return certification;
    }

    public void setCertification(Integer certification) {
        this.certification = certification;
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

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public Object getAvatar() {
        return avatar;
    }

    public void setAvatar(Object avatar) {
        this.avatar = avatar;
    }

    public int getIsVip() {
        return isVip;
    }

    public void setIsVip(int isVip) {
        this.isVip = isVip;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
