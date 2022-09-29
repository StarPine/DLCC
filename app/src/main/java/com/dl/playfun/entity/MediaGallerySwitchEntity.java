package com.dl.playfun.entity;

/**
 * Author: 彭石林
 * Time: 2022/9/21 18:41
 * Description: 相册价格配置video视频配置开关，
 */
public class MediaGallerySwitchEntity {
    //status:1为时间限制，2为运营限制
    private int bannedStatus;
    //禁止的结束时间，时间戳格式，北京时间
    private Long endDateTimestamp;

    public int getBannedStatus() {
        return bannedStatus;
    }

    public void setBannedStatus(int bannedStatus) {
        this.bannedStatus = bannedStatus;
    }

    public Long getEndDateTimestamp() {
        return endDateTimestamp;
    }

    public void setEndDateTimestamp(Long endDateTimestamp) {
        this.endDateTimestamp = endDateTimestamp;
    }
}
