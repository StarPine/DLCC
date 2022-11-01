package com.fine.friendlycc.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Author: 彭石林
 * Time: 2021/8/6 14:08
 * Description: 签到弹窗
 */
public class EjectEntity {

    @SerializedName("is_sign_in")
    private Integer isSignIn;
    @SerializedName("day_number")
    private Integer dayNumber;
    @SerializedName("is_first_sign")
    private Integer isFirstSign;

    //签到天数-男生
    private List<TaskConfigEntity.MaleConfigEntity> maleConfig;

    //签到天数-女生
    private List<Integer> femaleConfig;

    public Integer getFirstSign() {
        return isFirstSign;
    }

    public void setFirstSign(Integer firstSign) {
        isFirstSign = firstSign;
    }

    private EjectConfigEntity config;

    public EjectEntity(Integer isSignIn, Integer dayNumber, List<Integer> femaleConfig) {
        this.isSignIn = isSignIn;
        this.dayNumber = dayNumber;
        this.femaleConfig = femaleConfig;
    }

    public EjectEntity(Integer isSignIn, Integer dayNumber, List<TaskConfigEntity.MaleConfigEntity> maleConfig,List<Integer> femaleConfig) {
        this.isSignIn = isSignIn;
        this.dayNumber = dayNumber;
        this.maleConfig = maleConfig;
        this.femaleConfig = femaleConfig;
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


    public List<Integer> getFemaleConfig() {
        return femaleConfig;
    }

    public void setFemaleConfig(List<Integer> femaleConfig) {
        this.femaleConfig = femaleConfig;
    }

    public List<TaskConfigEntity.MaleConfigEntity> getMaleConfig() {
        return maleConfig;
    }

    public void setMaleConfig(List<TaskConfigEntity.MaleConfigEntity> maleConfig) {
        this.maleConfig = maleConfig;
    }
}
