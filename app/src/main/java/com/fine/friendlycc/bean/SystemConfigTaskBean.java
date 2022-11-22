package com.fine.friendlycc.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Author: 彭石林
 * Time: 2021/8/26 16:29
 * Description: This is SystemConfigTaskBean
 */
public class SystemConfigTaskBean {
    //入口文案(任务中心)
    @SerializedName("entry_label")
    private String entryLabel;
    //Head背景图(任务中心)
    @SerializedName("head_img2")
    private String headImg;
    //背景图(任务中心)
    @SerializedName("background_img2")
    private String backgroundImg;
    //附近弹窗
    @SerializedName("floating_img")
    private String floatingImg;

    public String getEntryLabel() {
        return entryLabel;
    }

    public void setEntryLabel(String entryLabel) {
        this.entryLabel = entryLabel;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getBackgroundImg() {
        return backgroundImg;
    }

    public void setBackgroundImg(String backgroundImg) {
        this.backgroundImg = backgroundImg;
    }

    public String getFloatingImg() {
        return floatingImg;
    }

    public void setFloatingImg(String floatingImg) {
        this.floatingImg = floatingImg;
    }

    @Override
    public String toString() {
        return "SystemConfigTaskBean{" +
                "entryLabel='" + entryLabel + '\'' +
                ", headImg='" + headImg + '\'' +
                ", backgroundImg='" + backgroundImg + '\'' +
                ", floatingImg='" + floatingImg + '\'' +
                '}';
    }
}