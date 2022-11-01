package com.fine.friendlycc.entity;

import com.google.gson.annotations.SerializedName;

public class GameCoinBuy {

    /**
     *{
     *             "id": 1,
     *             "goods_name": "X132",
     *             "google_goods_id": "1000zs",
     *             "apple_goods_id": "1007",
     *             "goods_label": "X66",
     *             "pay_price": "P3300",
     *             "price": "2.99",
     *             "is_first": 0
     * }
     */

    private int id;

    @SerializedName("goods_name")
    private String goodsName;

    @SerializedName("google_goods_id")
    private String googleGoodsId;
    @SerializedName("apple_goods_id")
    private String appleGoodsId;

    @SerializedName("goods_label")
    private String goodsLabel;

    @SerializedName("pay_price")
    private String payPrice;

    private String price;

    @SerializedName("actual_value")
    private  Integer actualValue;

    @SerializedName("is_first")
    private Integer isFirst;


    public Integer getIsFirst() {
        return isFirst;
    }

    public void setIsFirst(Integer isFirst) {
        this.isFirst = isFirst;
    }

    public Integer getActualValue() {
        return actualValue;
    }

    public void setActualValue(Integer actualValue) {
        this.actualValue = actualValue;
    }



    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGoogleGoodsId() {
        return googleGoodsId;
    }

    public void setGoogleGoodsId(String googleGoodsId) {
        this.googleGoodsId = googleGoodsId;
    }

    public String getAppleGoodsId() {
        return appleGoodsId;
    }

    public void setAppleGoodsId(String appleGoodsId) {
        this.appleGoodsId = appleGoodsId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }


    public String getPayPrice() {
        return payPrice;
    }

    public void setPayPrice(String payPrice) {
        this.payPrice = payPrice;
    }

    public String getGoodsLabel() {
        return goodsLabel;
    }

    public void setGoodsLabel(String goodsLabel) {
        this.goodsLabel = goodsLabel;
    }

    @Override
    public String toString() {
        return "GameCoinBuy{" +
                "id=" + id +
                ", goodsName='" + goodsName + '\'' +
                ", googleGoodsId='" + googleGoodsId + '\'' +
                ", appleGoodsId='" + appleGoodsId + '\'' +
                ", goodsLabel='" + goodsLabel + '\'' +
                ", payPrice='" + payPrice + '\'' +
                ", price='" + price + '\'' +
                ", actualValue=" + actualValue +
                ", isFirst=" + isFirst +
                '}';
    }
}
