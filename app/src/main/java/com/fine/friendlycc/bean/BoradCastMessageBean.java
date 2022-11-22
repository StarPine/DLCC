package com.fine.friendlycc.bean;

import com.google.gson.annotations.SerializedName;

/**
 * 电台消息
 *
 * @author litchi
 */
public class BoradCastMessageBean {

    private int id;
    @SerializedName("topical_id")
    private int topicalId;
    private String content;
    @SerializedName("created_at")
    private String createdAt;
    private ItemUserBean user;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public int getTopicalId() {
        return topicalId;
    }

    public void setTopicalId(int topicalId) {
        this.topicalId = topicalId;
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
}