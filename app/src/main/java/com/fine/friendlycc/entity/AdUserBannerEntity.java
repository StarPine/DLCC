package com.fine.friendlycc.entity;

import java.util.List;

/**
 * Author: 彭石林
 * Time: 2022/7/28 17:13
 * Description: This is AdUserBannerEntity
 */
public class AdUserBannerEntity {
    private List<AdUserItemEntity> dataList;

    public List<AdUserItemEntity> getDataList() {
        return dataList;
    }

    public void setDataList(List<AdUserItemEntity> dataList) {
        this.dataList = dataList;
    }
}
