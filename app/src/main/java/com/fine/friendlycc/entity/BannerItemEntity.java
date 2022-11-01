package com.fine.friendlycc.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class BannerItemEntity implements Serializable {
    private int id;
    private String title;
    private String img;
    private String position;
    @SerializedName("landing_page")
    private String landingPage;
    private String text;

    @SerializedName("open_time")
    private Integer openTime;

    public Integer getOpenTime() {
        return openTime;
    }

    public void setOpenTime(Integer openTime) {
        this.openTime = openTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getLandingPage() {
        return landingPage;
    }

    public void setLandingPage(String landingPage) {
        this.landingPage = landingPage;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
