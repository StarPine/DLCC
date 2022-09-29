package com.dl.playfun.entity;

import com.google.gson.annotations.SerializedName;

/**
 * 电台消息
 *
 * @author litchi
 */
public class BoradCastMessageEntity {

    private int id;
    @SerializedName("topical_id")
    private int topicalId;
    private String content;
    @SerializedName("created_at")
    private String createdAt;
    private ItemUserEntity user;

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

    public ItemUserEntity getUser() {
        return user;
    }

    public void setUser(ItemUserEntity user) {
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
