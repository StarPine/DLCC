package com.dl.playfun.entity;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.dl.playfun.BR;
import com.google.gson.annotations.SerializedName;

/**
 * 评论消息
 *
 * @author litchi
 */
public class EvaluateMessageEntity extends BaseObservable {

    private int id;
    @SerializedName("created_at")
    private String createdAt;
    private String content;
    @SerializedName("relation_type")
    private Integer relationType;
    private Integer status;
    private ItemUserEntity user;
    private ItemEvaluateEntity evaluate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public Integer getRelationType() {
        return relationType;
    }

    public void setRelationType(Integer relationType) {
        this.relationType = relationType;
    }

    @Bindable
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
        notifyPropertyChanged(BR.status);
    }

    public ItemEvaluateEntity getEvaluate() {
        return evaluate;
    }

    public void setEvaluate(ItemEvaluateEntity evaluate) {
        this.evaluate = evaluate;
    }
}
