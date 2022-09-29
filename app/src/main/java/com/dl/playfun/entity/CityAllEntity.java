package com.dl.playfun.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Author: 彭石林
 * Time: 2022/7/14 10:26
 * Description: This is CityAllEntity
 */
public class CityAllEntity {

    @SerializedName("city_all")
    private List<ConfigItemEntity> cityAll;

    public List<ConfigItemEntity> getCityAll() {
        return cityAll;
    }

    public void setCityAll(List<ConfigItemEntity> cityAll) {
        this.cityAll = cityAll;
    }
}
