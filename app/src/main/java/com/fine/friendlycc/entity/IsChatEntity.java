package com.fine.friendlycc.entity;

import com.google.gson.annotations.SerializedName;

public class IsChatEntity {

    /**
     * is_chant : 1
     * is_vip : 1
     * chat_number : 8
     */
    @SerializedName("is_chant")
    private int isChant;
    @SerializedName("is_vip")
    private int isVip;
    @SerializedName("chat_number")
    private int chatNumber;
    @SerializedName("can_get_chat_number")
    private Integer canGetChatNumber;

    public int getIsChant() {
        return isChant;
    }

    public void setIsChant(int isChant) {
        this.isChant = isChant;
    }

    public int getIsVip() {
        return isVip;
    }

    public void setIsVip(int isVip) {
        this.isVip = isVip;
    }

    public int getChatNumber() {
        return chatNumber;
    }

    public void setChatNumber(int chatNumber) {
        this.chatNumber = chatNumber;
    }

    public Integer getCanGetChatNumber() {
        return canGetChatNumber;
    }

    public void setCanGetChatNumber(Integer canGetChatNumber) {
        this.canGetChatNumber = canGetChatNumber;
    }
}
