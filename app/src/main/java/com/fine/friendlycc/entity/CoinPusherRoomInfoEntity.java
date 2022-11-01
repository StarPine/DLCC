package com.fine.friendlycc.entity;

import java.util.List;

/**
 * Author: 彭石林
 * Time: 2022/8/24 10:47
 * Description: 推币机房间列表
 */
public class CoinPusherRoomInfoEntity {
    //设备列表
    private List<CoinPusherRoomDeviceInfo> list;

    public List<CoinPusherRoomDeviceInfo> getList() {
        return list;
    }

    public void setList(List<CoinPusherRoomDeviceInfo> list) {
        this.list = list;
    }

}
