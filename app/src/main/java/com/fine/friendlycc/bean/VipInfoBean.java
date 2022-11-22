package com.fine.friendlycc.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class VipInfoBean implements Serializable {


    private BannerBean banner;
    @SerializedName("is_vip")
    private int isVip;
    @SerializedName("end_time")
    private String endTime;
    @SerializedName("list")
    private List<VipPackageItemBean> list;

    public BannerBean getBanner() {
        return banner;
    }

    public void setBanner(BannerBean banner) {
        this.banner = banner;
    }

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

    public List<VipPackageItemBean> getList() {
        return list;
    }

    public void setList(List<VipPackageItemBean> list) {
        this.list = list;
    }

    public static class BannerBean implements Serializable {

        private int id;
        private String title;
        private String img;
        private Object link;
        private int sort;
        @SerializedName("ad_type")
        private int adType;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public Object getLink() {
            return link;
        }

        public void setLink(Object link) {
            this.link = link;
        }

        public int getSort() {
            return sort;
        }

        public void setSort(int sort) {
            this.sort = sort;
        }

        public int getAdType() {
            return adType;
        }

        public void setAdType(int adType) {
            this.adType = adType;
        }
    }
}