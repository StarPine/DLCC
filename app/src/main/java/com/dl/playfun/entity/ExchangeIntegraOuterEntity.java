package com.dl.playfun.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Author: 彭石林
 * Time: 2021/9/23 16:31
 * Description: This is ExchangeIntegraOuterEntity
 */
public class ExchangeIntegraOuterEntity {
    @SerializedName("total_coin")
    private Integer totalCoin;
    @SerializedName("total_bonus")
    private Integer totalBonus;
    private List<ExchangeIntegraEntity> data;

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

    public List<ExchangeIntegraEntity> getData() {
        return data;
    }

    public void setData(List<ExchangeIntegraEntity> data) {
        this.data = data;
    }
}
