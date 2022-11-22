package com.fine.friendlycc.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Author: 彭石林
 * Time: 2021/10/26 14:17
 * Description: This is BroadcastListBean
 */
public class BroadcastListBean {
    //真人集合
    @SerializedName("real_data")
    private List<BroadcastBean> realData;
    //机器人
    @SerializedName("untrue_data")
    private List<BroadcastBean> untrueData;
    //是否有追踪的人
    @SerializedName("is_collect")
    private Integer isCollect;

    public Integer getIsCollect() {
        return isCollect;
    }

    public void setIsCollect(Integer isCollect) {
        this.isCollect = isCollect;
    }

    public List<BroadcastBean> getRealData() {
        return realData;
    }

    public void setRealData(List<BroadcastBean> realData) {
        this.realData = realData;
    }

    public List<BroadcastBean> getUntrueData() {
        return untrueData;
    }

    public void setUntrueData(List<BroadcastBean> untrueData) {
        this.untrueData = untrueData;
    }

}