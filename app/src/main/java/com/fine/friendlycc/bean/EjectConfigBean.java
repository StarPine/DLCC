package com.fine.friendlycc.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Author: 彭石林
 * Time: 2021/8/6 14:08
 * Description: 签到弹窗 详细配置
 */
public class EjectConfigBean {

    @SerializedName("day_1")
    private Integer day1;
    @SerializedName("day_2")
    private Integer day2;
    @SerializedName("day_3")
    private Integer day3;
    @SerializedName("day_4")
    private Integer day4;
    @SerializedName("day_5")
    private Integer day5;
    @SerializedName("day_6")
    private Integer day6;
    @SerializedName("day_7")
    private Integer day7;

    public Integer getDay1() {
        return day1;
    }

    public void setDay1(Integer day1) {
        this.day1 = day1;
    }

    public Integer getDay2() {
        return day2;
    }

    public void setDay2(Integer day2) {
        this.day2 = day2;
    }

    public Integer getDay3() {
        return day3;
    }

    public void setDay3(Integer day3) {
        this.day3 = day3;
    }

    public Integer getDay4() {
        return day4;
    }

    public void setDay4(Integer day4) {
        this.day4 = day4;
    }

    public Integer getDay5() {
        return day5;
    }

    public void setDay5(Integer day5) {
        this.day5 = day5;
    }

    public Integer getDay6() {
        return day6;
    }

    public void setDay6(Integer day6) {
        this.day6 = day6;
    }

    public Integer getDay7() {
        return day7;
    }

    public void setDay7(Integer day7) {
        this.day7 = day7;
    }
}