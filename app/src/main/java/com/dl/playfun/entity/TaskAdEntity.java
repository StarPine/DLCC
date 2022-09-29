package com.dl.playfun.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Author: 彭石林
 * Time: 2021/9/4 18:18
 * Description: This is TaskAdEntity
 */
public class TaskAdEntity {
    private String title;
    private String link;
    private String img;

    @SerializedName("ad_type")
    private int adType;

    private int sort;

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getAdType() {
        return adType;
    }

    public void setAdType(int adType) {
        this.adType = adType;
    }

    @Override
    public String toString() {
        return "TaskAdEntity{" +
                "title='" + title + '\'' +
                ", link='" + link + '\'' +
                ", img='" + img + '\'' +
                ", adType=" + adType +
                ", sort=" + sort +
                '}';
    }
}
