package com.fine.friendlycc.bean;

import java.io.Serializable;

public class UserProfileInfo implements Serializable {
    private Integer id;
    private String nickname;
    private String avatar;
    private Integer sex;
    private Integer isVip;
    private Integer certification;
    private Integer cityId;
    private String cityName;
    private Integer age;
    private String constellation;
    private String occupation;
    private Integer occupationId;
    private String gameChannel;

    public String getGameChannel() {
        return gameChannel;
    }

    public void setGameChannel(String gameChannel) {
        this.gameChannel = gameChannel;
    }

    public Integer getOccupationId() {
        return occupationId;
    }

    public void setOccupationId(Integer occupationId) {
        this.occupationId = occupationId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Integer getIsVip() {
        return isVip;
    }

    public void setIsVip(Integer isVip) {
        this.isVip = isVip;
    }

    public Integer getCertification() {
        return certification;
    }

    public void setCertification(Integer certification) {
        this.certification = certification;
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getConstellation() {
        return constellation;
    }

    public void setConstellation(String constellation) {
        this.constellation = constellation;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }
}
