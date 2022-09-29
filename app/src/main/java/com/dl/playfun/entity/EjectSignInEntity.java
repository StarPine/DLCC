package com.dl.playfun.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Author: 彭石林
 * Time: 2021/8/6 16:26
 * Description: 每日签到成功返回类
 */
public class EjectSignInEntity {

    @SerializedName("is_card")
    private Integer isCard;
    @SerializedName("day_number")
    private Integer dayNumber;
    private String text;
    private String msg;

    public Integer getIsCard() {
        return isCard;
    }

    public void setIsCard(Integer isCard) {
        this.isCard = isCard;
    }

    public Integer getDayNumber() {
        return dayNumber;
    }

    public void setDayNumber(Integer dayNumber) {
        this.dayNumber = dayNumber;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
