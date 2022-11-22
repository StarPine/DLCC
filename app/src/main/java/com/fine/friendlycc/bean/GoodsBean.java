package com.fine.friendlycc.bean;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.fine.friendlycc.BR;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class GoodsBean extends BaseObservable implements Serializable {

    /**
     * day_give_video_card : 0
     * day_give_num : 0
     * day_give_coin : 0
     * video_card : 0
     * id : 104
     * goods_name : x2,800
     * google_goods_id : tw.com.dl.gl.pettycoon.1006
     * apple_goods_id : 61
     * give_coin : 1400
     * actual_value : 1400
     * price : 2000.00
     * is_recommend : 0
     * goods_desc :
     * limit : 0
     * type : 1
     * discount_label : x1400
     * sale_price : 670
     * original_price : null
     * day_price : null
     * symbol : $
     * give_coin_title : 1400+1400
     */

    @SerializedName("id")
    private int id;
    @SerializedName("goods_name")
    private String goodsName;
    @SerializedName("google_goods_id")
    private String googleGoodsId;
    @SerializedName("apple_goods_id")
    private String appleGoodsId;
    @SerializedName("give_coin")
    private int giveCoin;
    @SerializedName("actual_value")
    private int actualValue;
    @SerializedName("price")
    private String price;
    @SerializedName("is_recommend")
    private int isRecommend;
    @SerializedName("goods_desc")
    private String goodsDesc;
    @SerializedName("limit")
    private int limit;
    @SerializedName("type")
    private int type;
    @SerializedName("discount_label")
    private String discountLabel;
    @SerializedName("sale_price")
    private String salePrice;
    @SerializedName("original_price")
    private Object originalPrice;
    @SerializedName("day_price")
    private Object dayPrice;
    @SerializedName("symbol")
    private String symbol;
    @SerializedName("give_coin_title")
    private String giveCoinTitle;

    private boolean isSelected;

    @SerializedName("day_give_video_card")
    private int dayGiveVideoCard;
    @SerializedName("day_give_num")
    private int dayGiveNum;
    @SerializedName("day_give_coin")
    private int dayGiveCoin;
    @SerializedName("video_card")
    private int videoCard;
    @SerializedName("gold_price")
    private int goldPrice;

    public int getGoldPrice() {
        return goldPrice;
    }

    public void setGoldPrice(int goldPrice) {
        this.goldPrice = goldPrice;
    }

    public int getDayGiveVideoCard() {
        return dayGiveVideoCard;
    }

    public void setDayGiveVideoCard(int dayGiveVideoCard) {
        this.dayGiveVideoCard = dayGiveVideoCard;
    }

    public int getDayGiveNum() {
        return dayGiveNum;
    }

    public void setDayGiveNum(int dayGiveNum) {
        this.dayGiveNum = dayGiveNum;
    }

    public int getDayGiveCoin() {
        return dayGiveCoin;
    }

    public void setDayGiveCoin(int dayGiveCoin) {
        this.dayGiveCoin = dayGiveCoin;
    }

    public int getVideoCard() {
        return videoCard;
    }

    public void setVideoCard(int videoCard) {
        this.videoCard = videoCard;
    }

    @Bindable
    public boolean getSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
        notifyPropertyChanged(BR.selected);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
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

    public int getGiveCoin() {
        return giveCoin;
    }

    public void setGiveCoin(int giveCoin) {
        this.giveCoin = giveCoin;
    }

    public int getActualValue() {
        return actualValue;
    }

    public void setActualValue(int actualValue) {
        this.actualValue = actualValue;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getIsRecommend() {
        return isRecommend;
    }

    public void setIsRecommend(int isRecommend) {
        this.isRecommend = isRecommend;
    }

    public String getGoodsDesc() {
        return goodsDesc;
    }

    public void setGoodsDesc(String goodsDesc) {
        this.goodsDesc = goodsDesc;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDiscountLabel() {
        return discountLabel;
    }

    public void setDiscountLabel(String discountLabel) {
        this.discountLabel = discountLabel;
    }

    public String getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(String salePrice) {
        this.salePrice = salePrice;
    }

    public Object getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(Object originalPrice) {
        this.originalPrice = originalPrice;
    }

    public Object getDayPrice() {
        return dayPrice;
    }

    public void setDayPrice(Object dayPrice) {
        this.dayPrice = dayPrice;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getGiveCoinTitle() {
        return giveCoinTitle;
    }

    public void setGiveCoinTitle(String giveCoinTitle) {
        this.giveCoinTitle = giveCoinTitle;
    }
}