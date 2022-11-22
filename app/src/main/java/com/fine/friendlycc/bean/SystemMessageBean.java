package com.fine.friendlycc.bean;

import com.google.gson.annotations.SerializedName;

/**
 * 系统消息
 *
 * @author litchi
 */
public class SystemMessageBean {


    private int id;
    /**
     * type 1注册 2邀请码 3 VIP续费
     */
    private int type;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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
}