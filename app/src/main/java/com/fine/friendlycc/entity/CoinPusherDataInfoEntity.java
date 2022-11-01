package com.fine.friendlycc.entity;

import java.io.Serializable;

/**
 * Author: 彭石林
 * Time: 2022/9/1 12:01
 * Description: 推币机
 */
public class CoinPusherDataInfoEntity implements Serializable {
    //金币余额
    private int totalGold;
    //outTime	int	不投币退出房间时间/秒
    private int outTime;
    //倒计时提示toast
    private int countdown;
    //拉流地址
    private String rtcUrl;
    //拉流客户端ID
    private String clientWsRtcId;
    //房间信息
    private CoinPusherRoomDeviceInfo roomInfo;


    public int getTotalGold() {
        return totalGold;
    }

    public void setTotalGold(int totalGold) {
        this.totalGold = totalGold;
    }

    public int getOutTime() {
        return outTime;
    }

    public void setOutTime(int outTime) {
        this.outTime = outTime;
    }

    public int getCountdown() {
        return countdown;
    }

    public void setCountdown(int countdown) {
        this.countdown = countdown;
    }

    public String getRtcUrl() {
        return rtcUrl;
    }

    public void setRtcUrl(String rtcUrl) {
        this.rtcUrl = rtcUrl;
    }

    public String getClientWsRtcId() {
        return clientWsRtcId;
    }

    public void setClientWsRtcId(String clientWsRtcId) {
        this.clientWsRtcId = clientWsRtcId;
    }

    public CoinPusherRoomDeviceInfo getRoomInfo() {
        return roomInfo;
    }

    public void setRoomInfo(CoinPusherRoomDeviceInfo roomInfo) {
        this.roomInfo = roomInfo;
    }

}
