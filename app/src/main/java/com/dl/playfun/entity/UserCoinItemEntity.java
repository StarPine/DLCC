package com.dl.playfun.entity;

import com.google.gson.annotations.SerializedName;

public class UserCoinItemEntity {

    private int id;
    private int money;
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

    public ItemUserEntity getUser() {
        return user;
    }

    public void setUser(ItemUserEntity user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "UserCoinItemEntity{" +
                "id=" + id +
                ", money=" + money +
                ", content='" + content + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", user=" + user +
                '}';
    }
}
