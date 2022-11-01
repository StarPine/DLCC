package com.fine.friendlycc.entity;

import java.util.List;

/**
 * Author: 彭石林
 * Time: 2022/7/25 16:11
 * Description: This is AdBannerEntity
 */
public class AdBannerEntity {

    private List<AdItemEntity> dataList;

    public List<AdItemEntity> getDataList() {
        return dataList;
    }

    public void setDataList(List<AdItemEntity> dataList) {
        this.dataList = dataList;
    }
}
