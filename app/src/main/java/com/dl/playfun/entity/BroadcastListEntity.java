package com.dl.playfun.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Author: 彭石林
 * Time: 2021/10/26 14:17
 * Description: This is BroadcastListEntity
 */
public class BroadcastListEntity {
    //真人集合
    @SerializedName("real_data")
    private List<BroadcastEntity> realData;
    //机器人
    @SerializedName("untrue_data")
    private List<BroadcastEntity> untrueData;
    //是否有追踪的人
    @SerializedName("is_collect")
    private Integer isCollect;

    public Integer getIsCollect() {
        return isCollect;
    }

    public void setIsCollect(Integer isCollect) {
        this.isCollect = isCollect;
    }

    public List<BroadcastEntity> getRealData() {
        return realData;
    }

    public void setRealData(List<BroadcastEntity> realData) {
        this.realData = realData;
    }

    public List<BroadcastEntity> getUntrueData() {
        return untrueData;
    }

    public void setUntrueData(List<BroadcastEntity> untrueData) {
        this.untrueData = untrueData;
    }

}
