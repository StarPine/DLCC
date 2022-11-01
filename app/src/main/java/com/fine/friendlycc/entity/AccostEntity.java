package com.fine.friendlycc.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Author: 彭石林
 * Time: 2021/11/30 17:57
 * Description: 搭讪实体类
 */
public class AccostEntity {
    //能否搭讪
    private Integer submit;
    //冷却时间
    @SerializedName("expiration_time")
    private String expirationTime;
    //换一批间隔时间(秒)
    @SerializedName("change_time")
    private int changeTime;
    @SerializedName("date_time")
    private String dateTime;

    private List<AccostItemEntity> data;

    public Integer getSubmit() {
        if (submit == null) {
            return 0;
        }
        return submit;
    }

    public void setSubmit(Integer submit) {
        this.submit = submit;
    }

    public List<AccostItemEntity> getData() {
        return data;
    }

    public void setData(List<AccostItemEntity> data) {
        this.data = data;
    }

    public String getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(String expirationTime) {
        this.expirationTime = expirationTime;
    }

    public int getChangeTime() {
        return changeTime;
    }

    public void setChangeTime(int changeTime) {
        this.changeTime = changeTime;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}
