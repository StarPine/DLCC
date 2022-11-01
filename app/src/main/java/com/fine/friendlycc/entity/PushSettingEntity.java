package com.fine.friendlycc.entity;

import com.fine.friendlycc.data.typeadapter.BooleanTypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;

/**
 * 推送设置
 *
 * @author wulei
 */
public class PushSettingEntity {

    private Integer id;
    @SerializedName("user_id")
    private Integer userId;
    @SerializedName("is_chat")
    @JsonAdapter(BooleanTypeAdapter.class)
    private Boolean isChat;
    @SerializedName("is_sign")
    @JsonAdapter(BooleanTypeAdapter.class)
    private Boolean isSign;
    @SerializedName("is_give")
    @JsonAdapter(BooleanTypeAdapter.class)
    private Boolean isGive;
    @SerializedName("is_comment")
    @JsonAdapter(BooleanTypeAdapter.class)
    private Boolean isComment;
    @SerializedName("is_broadcast")
    @JsonAdapter(BooleanTypeAdapter.class)
    private Boolean isBroadcast;
    @SerializedName("is_apply")
    @JsonAdapter(BooleanTypeAdapter.class)
    private Boolean isApply;
    @SerializedName("is_invitation")
    @JsonAdapter(BooleanTypeAdapter.class)
    private Boolean isInvitation;
    @SerializedName("is_sound")
    @JsonAdapter(BooleanTypeAdapter.class)
    private Boolean isSound;
    @SerializedName("is_shake")
    @JsonAdapter(BooleanTypeAdapter.class)
    private Boolean isShake;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("updated_at")
    private String updatedAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Boolean getChat() {
        return isChat;
    }

    public void setChat(Boolean chat) {
        isChat = chat;
    }

    public Boolean getSign() {
        return isSign;
    }

    public void setSign(Boolean sign) {
        isSign = sign;
    }

    public Boolean getGive() {
        return isGive;
    }

    public void setGive(Boolean give) {
        isGive = give;
    }

    public Boolean getComment() {
        return isComment;
    }

    public void setComment(Boolean comment) {
        isComment = comment;
    }

    public Boolean getBroadcast() {
        return isBroadcast;
    }

    public void setBroadcast(Boolean broadcast) {
        isBroadcast = broadcast;
    }

    public Boolean getApply() {
        return isApply;
    }

    public void setApply(Boolean apply) {
        isApply = apply;
    }

    public Boolean getInvitation() {
        return isInvitation;
    }

    public void setInvitation(Boolean invitation) {
        isInvitation = invitation;
    }

    public Boolean getSound() {
        return isSound;
    }

    public void setSound(Boolean sound) {
        isSound = sound;
    }

    public Boolean getShake() {
        return isShake;
    }

    public void setShake(Boolean shake) {
        isShake = shake;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
