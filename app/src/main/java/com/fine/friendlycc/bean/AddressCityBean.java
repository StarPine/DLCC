package com.fine.friendlycc.bean;

import java.util.List;

/**
 * Author: 彭石林
 * Time: 2021/8/13 16:59
 * Description: This is AddressCityBean
 */
public class AddressCityBean {
    private boolean isChoose = false;
    private String city;
    private List<AddressCityItemBean> regions;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public List<AddressCityItemBean> getRegions() {
        return regions;
    }

    public void setRegions(List<AddressCityItemBean> regions) {
        this.regions = regions;
    }

    public boolean getIsChoose() {
        return isChoose;
    }

    public void setIsChoose(boolean isChoose) {
        this.isChoose = isChoose;
    }
}