package com.fine.friendlycc.bean;

import com.google.gson.annotations.SerializedName;

/**
 * 收益提醒消息
 *
 * @author wulei
 */
public class ProfitMessageBean {

    private int id;
    @SerializedName("created_at")
    private String createdAt;
    private String content;
    private ItemUserBean user;
    /**
     * 1相册收益 2红包照片收益 money 金额
     */
    private Integer type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }


    public ItemUserBean getUser() {
        return user;
    }

    public void setUser(ItemUserBean user) {
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }


}