package com.fine.friendlycc.bean;

import com.google.gson.annotations.SerializedName;

/**
 * @ClassName TagBean
 * @Description TODO
 * @Author 彭石林
 * @Date 2021/7/3 10:46
 * @Phone 16620350375
 * @email 15616314565@163.com
 * @Version 1.0
 **/
public class TagBean {

    @SerializedName("this_is_gg")
    private Integer thisIsGg; //当前用户是否GG 0否 1是

    @SerializedName("to_is_gg")
    private Integer toIsGg; //	对方用户是否GG 0否 1是
    @SerializedName("to_is_invite")
    private Integer toIsInvite; //	对方用户是否填写邀请码 0否 1是
    @SerializedName("is_online")
    private Integer isOnline;
    @SerializedName("calling_status")
    private Integer callingStatus;
    @SerializedName("is_blacklist")
    private Integer isBlacklist;
    @SerializedName("blacklist_status")
    private Integer blacklistStatus;
    @SerializedName("is_collect")
    private int isCollect;
    private Integer isChatPush;
    //当前是否是常联系
    private Integer isContact;

    public Integer getIsContact() {
        return isContact;
    }

    public void setIsContact(Integer isContact) {
        this.isContact = isContact;
    }

    public Integer getIsCollect() {
        return isCollect;
    }

    public void setIsCollect(Integer isCollect) {
        this.isCollect = isCollect;
    }

    public Integer getIsChatPush() {
        return isChatPush;
    }

    public void setIsChatPush(Integer isChatPush) {
        this.isChatPush = isChatPush;
    }

    public Integer getIsBlacklist() {
        return isBlacklist;
    }

    public void setIsBlacklist(Integer isBlacklist) {
        this.isBlacklist = isBlacklist;
    }

    public Integer getBlacklistStatus() {
        return blacklistStatus;
    }

    public void setBlacklistStatus(Integer blacklistStatus) {
        this.blacklistStatus = blacklistStatus;
    }

    public Integer getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(Integer isOnline) {
        this.isOnline = isOnline;
    }

    public Integer getCallingStatus() {
        return callingStatus;
    }

    public void setCallingStatus(Integer callingStatus) {
        this.callingStatus = callingStatus;
    }

    public Integer getThisIsGg() {
        return thisIsGg;
    }

    public void setThisIsGg(Integer thisIsGg) {
        this.thisIsGg = thisIsGg;
    }

    public Integer getToIsGg() {
        return toIsGg;
    }

    public void setToIsGg(Integer toIsGg) {
        this.toIsGg = toIsGg;
    }

    public Integer getToIsInvite() {
        return toIsInvite;
    }

    public void setToIsInvite(Integer toIsInvite) {
        this.toIsInvite = toIsInvite;
    }
}