package com.fine.friendlycc.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RecommendUserBean implements Serializable {
    private int id;
    private String nickname;
    private String avatar;
    private int age;
    @SerializedName("city_name")
    private String cityName;

    public RecommendUserBean(int id, String nickname, String avatar, int age, String cityName) {
        this.id = id;
        this.nickname = nickname;
        this.avatar = avatar;
        this.age = age;
        this.cityName = cityName;
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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
}