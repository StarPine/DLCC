package com.fine.friendlycc.bean;

import com.google.gson.annotations.SerializedName;

/**
 * 消息分组
 *
 * @author wulei
 */
public class MessageGroupBean {

    /**
     * mold类型 system面具消息 sign报名消息 give点赞消息 evaluate评价消息 comment评论消息 broadcast电台消息 apply查看申请消息 profit收益消息
     */
    private String mold;
    private String content;
    @SerializedName("unread_number")
    private int unreadNumber;
    @SerializedName("created_at")
    private String createdAt;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMold() {
        return mold;
    }

    public void setMold(String mold) {
        this.mold = mold;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public int getUnreadNumber() {
        return unreadNumber;
    }

    public void setUnreadNumber(int unreadNumber) {
        this.unreadNumber = unreadNumber;
    }
}