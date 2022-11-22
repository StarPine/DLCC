package com.fine.friendlycc.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Author: 彭石林
 * Time: 2021/10/22 16:44
 * Description: This is PhotoAlbumItemBean
 */
public class PhotoAlbumItemBean {
    private Integer id;
    private String src;
    @SerializedName("created_at")
    private String createdAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}