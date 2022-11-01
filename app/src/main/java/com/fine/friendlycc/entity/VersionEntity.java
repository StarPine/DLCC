package com.fine.friendlycc.entity;

import androidx.databinding.BaseObservable;

import com.google.gson.annotations.SerializedName;

/**
 * @ClassName VersionEntity
 * @Description TODO
 * @Author 彭石林
 * @Date 2021/3/31 19:28
 * @Phone 16620350375
 * @email 15616314565@163.com
 * @Version 1.0
 **/
public class VersionEntity extends BaseObservable {
    private Integer version_code;
    private String version_name;
    private String url;
    private String content;
    private Integer is_update;
    //跳转谷歌商店地址
    @SerializedName("link")
    private String linkUrl;


    public Integer getVersion_code() {
        return version_code;
    }

    public void setVersion_code(Integer version_code) {
        this.version_code = version_code;
    }

    public String getVersion_name() {
        return version_name;
    }

    public void setVersion_name(String version_name) {
        this.version_name = version_name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getIs_update() {
        return is_update;
    }

    public void setIs_update(Integer is_update) {
        this.is_update = is_update;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    @Override
    public String toString() {
        return "VersionEntity{" +
                "version_code=" + version_code +
                ", version_name='" + version_name + '\'' +
                ", url='" + url + '\'' +
                ", content='" + content + '\'' +
                ", is_update=" + is_update +
                ", linkUrl='" + linkUrl + '\'' +
                '}';
    }
}