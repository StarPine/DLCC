package com.tencent.custom;

import com.google.gson.annotations.SerializedName;

/**
 * Author: 彭石林
 * Time: 2021/12/3 17:01
 * Description: This is GiftEntity
 */
public class GiftEntity {
    private String title;
    private Integer amount;
    @SerializedName("img_path")
    private String imgPath;
    @SerializedName("svga_path")
    private String svgaPath;
    private Integer price;
    @SerializedName("profit_diamond")
    private Integer profitDiamond;
    @SerializedName("profit_twd")
    private Double profitTwd;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getSvgaPath() {
        return svgaPath;
    }

    public void setSvgaPath(String svgaPath) {
        this.svgaPath = svgaPath;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getProfitDiamond() {
        return profitDiamond;
    }

    public void setProfitDiamond(Integer profitDiamond) {
        this.profitDiamond = profitDiamond;
    }

    public Double getProfitTwd() {
        return profitTwd;
    }

    public void setProfitTwd(Double profitTwd) {
        this.profitTwd = profitTwd;
    }

    @Override
    public String toString() {
        return "GiftEntity{" +
                "title='" + title + '\'' +
                ", amount=" + amount +
                ", imgPath='" + imgPath + '\'' +
                ", svgaPath='" + svgaPath + '\'' +
                ", price=" + price +
                ", profitDiamond=" + profitDiamond +
                ", profitTwd=" + profitTwd +
                '}';
    }
}
