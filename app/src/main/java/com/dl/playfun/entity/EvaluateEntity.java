package com.dl.playfun.entity;

import com.google.gson.annotations.SerializedName;

public class EvaluateEntity {

    /**
     * tag_id : 1
     * number : 2
     */
    @SerializedName("tag_id")
    private int tagId;
    private int number;

    public int getTagId() {
        return tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
