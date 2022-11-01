package com.fine.friendlycc.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * Author: 彭石林
 * Time: 2022/9/15 16:05
 * Description: This is MediaPayPerConfigEntity
 */
public class MediaPayPerConfigEntity implements Serializable{
    private itemTagEntity videoResource;
    private itemTagEntity photoResource;

    public itemTagEntity getVideo() {
        return videoResource;
    }

    public void setVideo(itemTagEntity video) {
        this.videoResource = video;
    }

    public itemTagEntity getPhoto() {
        return photoResource;
    }

    public void setPhoto(itemTagEntity photo) {
        this.photoResource = photo;
    }

    public static class itemTagEntity implements Serializable{
        private List<ItemEntity> configList;
        private Integer configId;

        public Integer getConfigId() {
            return configId;
        }

        public void setConfigId(Integer configId) {
            this.configId = configId;
        }

        public List<ItemEntity> getContent() {
            return configList;
        }

        public void setContent(List<ItemEntity> content) {
            this.configList = content;
        }
    }

    public static class ItemEntity implements Serializable {
        private String redPackageCoin;
        private BigDecimal redPackagProfit;
        private String configIndexString;

        public String getConfigIndexString() {
            return configIndexString;
        }

        public void setConfigIndexString(String configIndexString) {
            this.configIndexString = configIndexString;
        }

        public String getCoin() {
            return redPackageCoin;
        }

        public void setCoin(String coin) {
            this.redPackageCoin = coin;
        }

        public BigDecimal getProfit() {
            return redPackagProfit;
        }

        public void setProfit(BigDecimal profit) {
            this.redPackagProfit = profit;
        }
    }
}
