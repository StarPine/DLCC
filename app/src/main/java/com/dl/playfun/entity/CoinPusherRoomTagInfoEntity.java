package com.dl.playfun.entity;

import java.util.List;

/**
 * Author: 彭石林
 * Time: 2022/8/24 17:51
 * Description: This is CoinPusherRoomTagInfoEntity
 */
public class CoinPusherRoomTagInfoEntity {

    //金币余额
    private int totalGold;

    //设备级别列表
    private List<DeviceTag> tagArr;


    public int getTotalGold() {
        return totalGold;
    }

    public void setTotalGold(int totalGold) {
        this.totalGold = totalGold;
    }

    public List<DeviceTag> getTypeArr() {
        return tagArr;
    }

    public void setTypeArr(List<DeviceTag> tagArr) {
        this.tagArr = tagArr;
    }

    public static class DeviceTag{
        private int id;
        private String name;
        private int isRecommend;


        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getIsRecommend() {
            return isRecommend;
        }

        public void setIsRecommend(int isRecommend) {
            this.isRecommend = isRecommend;
        }
    }

}
