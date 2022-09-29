package com.dl.playfun.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Author: 彭石林
 * Time: 2021/8/4 10:28
 * Description: This is BrowseNumberEntity
 */
public class BrowseNumberEntity {

    @SerializedName("fans_number")
    private Integer fansNumber;
    @SerializedName("browse_number")
    private Integer browseNumber;
    @SerializedName("browse_time")
    private String browsetime;

    public String getBrowsetime() {
        return browsetime;
    }

    public void setBrowsetime(String browsetime) {
        this.browsetime = browsetime;
    }

    public Integer getFansNumber() {
        return fansNumber;
    }

    public void setFansNumber(Integer fansNumber) {
        this.fansNumber = fansNumber;
    }

    public Integer getBrowseNumber() {
        return browseNumber;
    }

    public void setBrowseNumber(Integer browseNumber) {
        this.browseNumber = browseNumber;
    }
}
