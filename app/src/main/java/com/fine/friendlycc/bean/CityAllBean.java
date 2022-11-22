package com.fine.friendlycc.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Author: 彭石林
 * Time: 2022/7/14 10:26
 * Description: This is CityAllBean
 */
public class CityAllBean {

    @SerializedName("city_all")
    private List<ConfigItemBean> cityAll;

    public List<ConfigItemBean> getCityAll() {
        return cityAll;
    }

    public void setCityAll(List<ConfigItemBean> cityAll) {
        this.cityAll = cityAll;
    }
}