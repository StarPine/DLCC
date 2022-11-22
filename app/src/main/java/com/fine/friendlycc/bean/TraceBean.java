package com.fine.friendlycc.bean;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.fine.friendlycc.BR;
import com.fine.friendlycc.data.typeadapter.BooleanTypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;

/**
 * Author: 彭石林
 * Time: 2021/8/3 11:17
 * Description: This is TraceBean
 */
public class TraceBean extends BaseObservable {
    private Integer id;
    private String nickname;
    private Integer sex;
    private String avatar;
    private Integer age;
    @SerializedName("is_vip")
    private Integer isVip;
    private String constellation;
    private String city;
    @SerializedName("game_channel")
    private String gameChannel;

    private Integer certification;

    @JsonAdapter(BooleanTypeAdapter.class)
    @SerializedName("is_follow")
    private Boolean isFollow;

    @SerializedName("time")
    private String time;

    private Integer number;

    public String getGameChannel() {
        return gameChannel;
    }

    public void setGameChannel(String gameChannel) {
        this.gameChannel = gameChannel;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    @Bindable
    public Boolean getFollow() {
        return isFollow;
    }

    public void setFollow(Boolean follow) {
        isFollow = follow;
        notifyPropertyChanged(BR.follow);
    }

    public Integer getCertification() {
        return certification;
    }

    public void setCertification(Integer certification) {
        this.certification = certification;
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

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
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

    public Integer getIsVip() {
        return isVip;
    }

    public void setIsVip(Integer isVip) {
        this.isVip = isVip;
    }

    public String getConstellation() {
        return constellation;
    }

    public void setConstellation(String constellation) {
        this.constellation = constellation;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

}