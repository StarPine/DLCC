package com.dl.playfun.entity;

import com.google.gson.annotations.SerializedName;

/**
 * @ClassName MessageTagEntity
 * @Description TODO
 * @Author 彭石林
 * @Date 2021/7/5 14:56
 * @Phone 16620350375
 * @email 15616314565@163.com
 * @Version 1.0
 **/
public class MessageTagEntity {
    @SerializedName("user_id")
    private Integer userId;

    private String nickname;
    private String avatar;
    private Integer age;
    @SerializedName("city_id")
    private Integer cityId;
    @SerializedName("occupation_id")
    private Integer occupationId;

    private String constellation;
    private Integer sex;
    @SerializedName("is_vip")
    private Integer isVip;
    private Integer certification;
    private String msg;

    @Override
    public String toString() {
        return "MessageTagEntity{" +
                "userId=" + userId +
                ", nickname='" + nickname + '\'' +
                ", avatar='" + avatar + '\'' +
                ", age=" + age +
                ", cityId=" + cityId +
                ", occupationId=" + occupationId +
                ", constellation='" + constellation + '\'' +
                ", sex=" + sex +
                ", isVip=" + isVip +
                ", certification=" + certification +
                ", msg='" + msg + '\'' +
                '}';
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

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public Integer getOccupationId() {
        return occupationId;
    }

    public void setOccupationId(Integer occupationId) {
        this.occupationId = occupationId;
    }

    public String getConstellation() {
        return constellation;
    }

    public void setConstellation(String constellation) {
        this.constellation = constellation;
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

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}