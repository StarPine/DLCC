package com.fine.friendlycc.bean;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.fine.friendlycc.BR;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;


/**
 * @author wulei
 */
public class VipPackageItemBean extends BaseObservable implements Serializable {

    /**
     * id : 25
     * goods_name : 1年
     * google_goods_id : vip7190az
     * apple_goods_id : vip7190
     * give_coin : 144000
     * actual_value : 365
     * price : 239.99
     * is_recommend : 0
     * discount_label : 限时2.7折
     * sale_price : 7190
     * original_price : 原價26280
     * day_price : 20/天
     * symbol : $￥
     * goods_tab : 冲1000送1W
     * privileges : [{"title":"特权1","desc":"的32131","img":"images/3134d5f0660b975cd1de768c159f1d9f.webp"}]
     */

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
    private String price;
    @SerializedName("is_recommend")
    private Integer isRecommend;
    @SerializedName("discount_label")
    private String discountLabel;
    @SerializedName("sale_price")
    private String salePrice;
    @SerializedName("original_price")
    private String originalPrice;
    @SerializedName("day_price")
    private String dayPrice;
    @SerializedName("symbol")
    private String symbol;
    @SerializedName("goods_tab")
    private String goodsTab;
    @SerializedName("privileges")
    private List<PrivilegesBean> privileges;
    @SerializedName("day_give_video_card")
    private int dayGiveVideoCard;
    @SerializedName("day_give_num")
    private int dayGiveNum;
    @SerializedName("day_give_coin")
    private int dayGiveCoin;
    @SerializedName("video_card")
    private int videoCard;

    private Boolean isSelected;
    //首充状态（H5传递）
    private Integer purchased;



    public Integer getPurchased() {
        return purchased;
    }

    public void setPurchased(Integer purchased) {
        this.purchased = purchased;
    }

    @Bindable
    public Boolean getSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
        notifyPropertyChanged(BR.selected);
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

    public Integer getIsRecommend() {
        return isRecommend;
    }

    public void setIsRecommend(Integer isRecommend) {
        this.isRecommend = isRecommend;
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

    public String getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(String originalPrice) {
        this.originalPrice = originalPrice;
    }

    public String getDayPrice() {
        return dayPrice;
    }

    public void setDayPrice(String dayPrice) {
        this.dayPrice = dayPrice;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getGoodsTab() {
        return goodsTab;
    }

    public void setGoodsTab(String goodsTab) {
        this.goodsTab = goodsTab;
    }

    public List<PrivilegesBean> getPrivileges() {
        return privileges;
    }

    public void setPrivileges(List<PrivilegesBean> privileges) {
        this.privileges = privileges;
    }

    public static class PrivilegesBean implements Serializable {
        /**
         * title : 特权1
         * desc : 的32131
         * img : images/3134d5f0660b975cd1de768c159f1d9f.webp
         */

        @SerializedName("title")
        private String title;
        @SerializedName("desc")
        private String desc;
        @SerializedName("img")
        private String img;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }
    }
}