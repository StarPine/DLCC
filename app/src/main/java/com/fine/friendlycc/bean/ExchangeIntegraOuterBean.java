package com.fine.friendlycc.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Author: 彭石林
 * Time: 2021/9/23 16:31
 * Description: This is ExchangeIntegraOuterBean
 */
public class ExchangeIntegraOuterBean {
    @SerializedName("total_coin")
    private Integer totalCoin;
    @SerializedName("total_bonus")
    private Integer totalBonus;
    private List<ExchangeIntegraBean> data;

    public Integer getTotalCoin() {
        return totalCoin;
    }

    public void setTotalCoin(Integer totalCoin) {
        this.totalCoin = totalCoin;
    }

    public Integer getTotalBonus() {
        return totalBonus;
    }

    public void setTotalBonus(Integer totalBonus) {
        this.totalBonus = totalBonus;
    }

    public List<ExchangeIntegraBean> getData() {
        return data;
    }

    public void setData(List<ExchangeIntegraBean> data) {
        this.data = data;
    }
}