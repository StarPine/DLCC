package com.fine.friendlycc.entity;

import com.google.gson.annotations.SerializedName;

/**
 * @author litchi
 */
public class ItemEvaluateEntity {
    private int id;
    @SerializedName("tag_id")
    private int tagId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTagId() {
        return tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }
}
