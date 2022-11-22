package com.fine.friendlycc.bean;

import com.google.gson.annotations.SerializedName;

public class UserCoinItemBean {

    private int id;
    private int money;
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

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
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

    @Override
    public String toString() {
        return "UserCoinItemBean{" +
                "id=" + id +
                ", money=" + money +
                ", content='" + content + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", user=" + user +
                '}';
    }
}