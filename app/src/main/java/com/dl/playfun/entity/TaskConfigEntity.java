package com.dl.playfun.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Author: 彭石林
 * Time: 2021/8/7 15:59
 * Description: 任务中心配置类
 */
public class TaskConfigEntity {
    //每日任务
    List<TaskConfigItemEntity> task;
    @SerializedName("is_sign_in")
    private Integer isSignIn;
    @SerializedName("day_number")
    private Integer dayNumber;
    @SerializedName("total_money")
    private Integer totalMoney;
    //签到天数-女生
//    private EjectConfigEntity config;
    @SerializedName("female_config")
    private List<Integer> femaleConfig;
    //签到天数-男生
    @SerializedName("male_config")
    private List<MaleConfigEntity> maleConfig;
    //邀请码
    private String code;
    //福袋页面
    @SerializedName("good_bag_url")
    private String goodBagUrl;

    @SerializedName("is_first_sign")
    private Integer isFirstSign;

    //Head背景图(任务中心)
    @SerializedName("head_img")
    private String headImg;
    //背景图(任务中心)
    @SerializedName("background_img")
    private String backgroundImg;

    public List<Integer> getFemaleConfig() {
        return femaleConfig;
    }

    public void setFemaleConfig(List<Integer> femaleConfig) {
        this.femaleConfig = femaleConfig;
    }

    public Integer getFirstSign() {
        return isFirstSign;
    }

    public void setFirstSign(Integer firstSign) {
        isFirstSign = firstSign;
    }

    public String getGoodBagUrl() {
        return goodBagUrl;
    }

    public void setGoodBagUrl(String goodBagUrl) {
        this.goodBagUrl = goodBagUrl;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getIsSignIn() {
        return isSignIn;
    }

    public void setIsSignIn(Integer isSignIn) {
        this.isSignIn = isSignIn;
    }

    public Integer getDayNumber() {
        return dayNumber;
    }

    public void setDayNumber(Integer dayNumber) {
        this.dayNumber = dayNumber;
    }

//    public EjectConfigEntity getConfig() {
//        return config;
//    }
//
//    public void setConfig(EjectConfigEntity config) {
//        this.config = config;
//    }

    public Integer getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(Integer totalMoney) {
        this.totalMoney = totalMoney;
    }

    public List<TaskConfigItemEntity> getTask() {
        return task;
    }

    public void setTask(List<TaskConfigItemEntity> task) {
        this.task = task;
    }

    public List<MaleConfigEntity> getMaleConfig() {
        return maleConfig;
    }

    public void setMaleConfig(List<MaleConfigEntity> maleConfig) {
        this.maleConfig = maleConfig;
    }

    public class  MaleConfigEntity{
        private Integer value;
        private Integer type;

        public Integer getValue() {
            return value;
        }

        public void setValue(Integer value) {
            this.value = value;
        }

        public Integer getType() {
            return type;
        }

        public void setType(Integer type) {
            this.type = type;
        }
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getBackgroundImg() {
        return backgroundImg;
    }

    public void setBackgroundImg(String backgroundImg) {
        this.backgroundImg = backgroundImg;
    }
}
