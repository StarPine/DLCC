package com.fine.friendlycc.bean;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.fine.friendlycc.BR;
import com.google.gson.annotations.SerializedName;

/**
 * 评论消息
 *
 * @author litchi
 */
public class EvaluateMessageBean extends BaseObservable {

    private int id;
    @SerializedName("created_at")
    private String createdAt;
    private String content;
    @SerializedName("relation_type")
    private Integer relationType;
    private Integer status;
    private ItemUserBean user;
    private ItemEvaluateBean evaluate;

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

    public ItemUserBean getUser() {
        return user;
    }

    public void setUser(ItemUserBean user) {
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

    public ItemEvaluateBean getEvaluate() {
        return evaluate;
    }

    public void setEvaluate(ItemEvaluateBean evaluate) {
        this.evaluate = evaluate;
    }
}