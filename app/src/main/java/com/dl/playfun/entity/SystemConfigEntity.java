package com.dl.playfun.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * 系统配置参数
 *
 * @author litchi
 */
public class SystemConfigEntity implements Serializable {

    private double CashOutServiceFee;
    private double CoinExchangeMoney;
    private Integer VideoRedPackageMoney;
    private Integer ImageRedPackageMoney;
    @SerializedName("man_user")
    private SystemRoleMoneyConfigEntity manUser;
    @SerializedName("man_real")
    private SystemRoleMoneyConfigEntity manReal;
    @SerializedName("man_vip")
    private SystemRoleMoneyConfigEntity manVip;
    @SerializedName("woman_user")
    private SystemRoleMoneyConfigEntity womanUser;
    @SerializedName("woman_real")
    private SystemRoleMoneyConfigEntity womanReal;
    @SerializedName("woman_vip")
    private SystemRoleMoneyConfigEntity womanVip;
    @SerializedName("delete_account")
    private int deleteAccount;
    private SystemConfigContentEntity content;

    //快照可看时间--2022-09-21
    private Integer photoSnapshotTime = 2;
    //VIP快照可看时间--2022-09-21
    private Integer photoSnapshotVIPTime;

    //会话列表限制数量
    @SerializedName("conversation_astrict_count")
    private Integer conversationAstrictCount;
    //默认区号
    @SerializedName("region_code")
    private String regionCode;
    /**
     * maleAccost : 1
     * femaleAccost : 1
     * registerMaleAccost : 0
     * registerFemaleAccost : 1
     */

    @SerializedName("maleAccost")
    private int maleAccost;
    @SerializedName("femaleAccost")
    private int femaleAccost;
    @SerializedName("registerMaleAccost")
    private int registerMaleAccost;
    @SerializedName("registerFemaleAccost")
    private int registerFemaleAccost;


    public Integer getPhotoSnapshotVIPTime() {
        return photoSnapshotVIPTime;
    }

    public void setPhotoSnapshotVIPTime(Integer photoSnapshotVIPTime) {
        this.photoSnapshotVIPTime = photoSnapshotVIPTime;
    }

    public Integer getPhotoSnapshotTime() {
        return photoSnapshotTime;
    }

    public void setPhotoSnapshotTime(Integer photoSnapshotTime) {
        this.photoSnapshotTime = photoSnapshotTime;
    }

    public int getMaleAccost() {
        return maleAccost;
    }

    public void setMaleAccost(int maleAccost) {
        this.maleAccost = maleAccost;
    }

    public int getFemaleAccost() {
        return femaleAccost;
    }

    public void setFemaleAccost(int femaleAccost) {
        this.femaleAccost = femaleAccost;
    }

    public int getRegisterMaleAccost() {
        return registerMaleAccost;
    }

    public void setRegisterMaleAccost(int registerMaleAccost) {
        this.registerMaleAccost = registerMaleAccost;
    }

    public int getRegisterFemaleAccost() {
        return registerFemaleAccost;
    }

    public void setRegisterFemaleAccost(int registerFemaleAccost) {
        this.registerFemaleAccost = registerFemaleAccost;
    }

    public int getDeleteAccount() {
        return deleteAccount;
    }

    public void setDeleteAccount(int deleteAccount) {
        this.deleteAccount = deleteAccount;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    public Integer getConversationAstrictCount() {
        return conversationAstrictCount;
    }

    public void setConversationAstrictCount(Integer conversationAstrictCount) {
        this.conversationAstrictCount = conversationAstrictCount;
    }

    public double getCashOutServiceFee() {
        return CashOutServiceFee;
    }

    public void setCashOutServiceFee(double cashOutServiceFee) {
        CashOutServiceFee = cashOutServiceFee;
    }

    public double getCoinExchangeMoney() {
        return CoinExchangeMoney;
    }

    public void setCoinExchangeMoney(double coinExchangeMoney) {
        CoinExchangeMoney = coinExchangeMoney;
    }

    public Integer getVideoRedPackageMoney() {
        return VideoRedPackageMoney;
    }

    public void setVideoRedPackageMoney(Integer videoRedPackageMoney) {
        VideoRedPackageMoney = videoRedPackageMoney;
    }

    public Integer getImageRedPackageMoney() {
        return ImageRedPackageMoney;
    }

    public void setImageRedPackageMoney(Integer imageRedPackageMoney) {
        ImageRedPackageMoney = imageRedPackageMoney;
    }

    public SystemRoleMoneyConfigEntity getManUser() {
        return manUser;
    }

    public void setManUser(SystemRoleMoneyConfigEntity manUser) {
        this.manUser = manUser;
    }

    public SystemRoleMoneyConfigEntity getManReal() {
        return manReal;
    }

    public void setManReal(SystemRoleMoneyConfigEntity manReal) {
        this.manReal = manReal;
    }

    public SystemRoleMoneyConfigEntity getManVip() {
        return manVip;
    }

    public void setManVip(SystemRoleMoneyConfigEntity manVip) {
        this.manVip = manVip;
    }

    public SystemRoleMoneyConfigEntity getWomanUser() {
        return womanUser;
    }

    public void setWomanUser(SystemRoleMoneyConfigEntity womanUser) {
        this.womanUser = womanUser;
    }

    public SystemRoleMoneyConfigEntity getWomanReal() {
        return womanReal;
    }

    public void setWomanReal(SystemRoleMoneyConfigEntity womanReal) {
        this.womanReal = womanReal;
    }

    public SystemRoleMoneyConfigEntity getWomanVip() {
        return womanVip;
    }

    public void setWomanVip(SystemRoleMoneyConfigEntity womanVip) {
        this.womanVip = womanVip;
    }

    public SystemConfigContentEntity getContent() {
        return content;
    }

    public void setContent(SystemConfigContentEntity content) {
        this.content = content;
    }
}
