package com.dl.playfun.entity;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.dl.playfun.BR;
import com.google.gson.annotations.SerializedName;

public class ChatRedPackageEntity extends BaseObservable {

    /**
     * user_id : 5
     * receive_user_id : 4
     * money : 10
     * desc : 小小啦
     * status : 1
     * updated_at : 2020-06-24 21:57:37
     * created_at : 2020-06-24 21:57:37
     * id : 2
     */
    private int id;
    @SerializedName("user_id")
    private int userId;
    private ItemUserEntity user;
    @SerializedName("receive_user_id")
    private String receiveUserId;
    private ItemUserEntity receiveuser;
    private int money;
    private String desc;
    private int status;
    @SerializedName("updated_at")
    private String updatedAt;
    @SerializedName("created_at")
    private String createdAt;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getReceiveUserId() {
        return receiveUserId;
    }

    public void setReceiveUserId(String receiveUserId) {
        this.receiveUserId = receiveUserId;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Bindable
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
        notifyPropertyChanged(BR.status);
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ItemUserEntity getUser() {
        return user;
    }

    public void setUser(ItemUserEntity user) {
        this.user = user;
    }

    public ItemUserEntity getReceiveuser() {
        return receiveuser;
    }

    public void setReceiveuser(ItemUserEntity receiveuser) {
        this.receiveuser = receiveuser;
    }
}
