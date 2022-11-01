package com.fine.friendlycc.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Author: 彭石林
 * Time: 2021/9/23 11:08
 * Description: This is ExchangeIntegraEntity
 */
public class ExchangeIntegraEntity {
    private Integer id;
    @SerializedName("bonus_name")
    private String bonusName;
    @SerializedName("coin_name")
    private String coinName;
    @SerializedName("coin_value")
    private Integer coinValue;
    @SerializedName("bonus_value")
    private Integer bonusValue;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBonusName() {
        return bonusName;
    }

    public void setBonusName(String bonusName) {
        this.bonusName = bonusName;
    }

    public String getCoinName() {
        return coinName;
    }

    public void setCoinName(String coinName) {
        this.coinName = coinName;
    }

    public Integer getCoinValue() {
        return coinValue;
    }

    public void setCoinValue(Integer coinValue) {
        this.coinValue = coinValue;
    }

    public Integer getBonusValue() {
        return bonusValue;
    }

    public void setBonusValue(Integer bonusValue) {
        this.bonusValue = bonusValue;
    }

}
