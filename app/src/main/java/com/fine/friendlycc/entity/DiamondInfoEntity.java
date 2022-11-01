package com.fine.friendlycc.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * 修改备注：钻石充值数据
 *
 * @Name： PlayFun_Google
 * @Description：
 * @Author： liaosf
 * @Date： 2022/8/23 11:01
 */
public class DiamondInfoEntity implements Serializable {

    /**
     * list : []
     * is_vip : 0
     * end_time : null
     * totalCoin : 1000
     */

    @SerializedName("is_vip")
    private int isVip;
    @SerializedName("end_time")
    private String endTime;
    @SerializedName("totalCoin")
    private int totalCoin;
    @SerializedName("list")
    private List<GoodsEntity> list;

    public int getIsVip() {
        return isVip;
    }

    public void setIsVip(int isVip) {
        this.isVip = isVip;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getTotalCoin() {
        return totalCoin;
    }

    public void setTotalCoin(int totalCoin) {
        this.totalCoin = totalCoin;
    }

    public List<GoodsEntity> getList() {
        return list;
    }

    public void setList(List<GoodsEntity> list) {
        this.list = list;
    }
}
