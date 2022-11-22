package com.fine.friendlycc.bean;

import com.google.gson.annotations.SerializedName;

/**
 * 点赞消息
 */
public class GiveMessageBean {

    private int id;
    @SerializedName("relation_id")
    private int relationId;
    @SerializedName("relation_type")
    private Integer relationType;
    @SerializedName("created_at")
    private String createdAt;
    private String content;
    private ItemUserBean user;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public int getRelationId() {
        return relationId;
    }

    public void setRelationId(int relationId) {
        this.relationId = relationId;
    }

    public Integer getRelationType() {
        return relationType;
    }

    public void setRelationType(Integer relationType) {
        this.relationType = relationType;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ItemUserBean getUser() {
        return user;
    }

    public void setUser(ItemUserBean user) {
        this.user = user;
    }
}