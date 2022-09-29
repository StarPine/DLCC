package com.dl.playfun.entity;

import androidx.databinding.BaseObservable;

import com.google.gson.annotations.SerializedName;

/**
 * @ClassName toUserHome
 * @Description TODO
 * @Author 彭石林
 * @Date 2021/6/28 11:37
 * @Phone 16620350375
 * @email 15616314565@163.com
 * @Version 1.0
 **/
public class ToUserHomeEntity extends BaseObservable {

    @SerializedName("is_browse")
    private Integer isBrowse;

    @SerializedName("more_number")
    private Integer moreNumber;

    public Integer getIsBrowse() {
        return isBrowse;
    }

    public void setIsBrowse(Integer isBrowse) {
        this.isBrowse = isBrowse;
    }

    public Integer getMoreNumber() {
        return moreNumber;
    }

    public void setMoreNumber(Integer moreNumber) {
        this.moreNumber = moreNumber;
    }
}