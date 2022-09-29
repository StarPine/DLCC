package com.dl.playfun.entity;

import java.util.List;

/**
 * Author: 彭石林
 * Time: 2021/8/13 16:59
 * Description: This is AddressCityEntity
 */
public class AddressCityEntity {
    private boolean isChoose = false;
    private String city;
    private List<AddressCityItemEntity> regions;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public List<AddressCityItemEntity> getRegions() {
        return regions;
    }

    public void setRegions(List<AddressCityItemEntity> regions) {
        this.regions = regions;
    }

    public boolean getIsChoose() {
        return isChoose;
    }

    public void setIsChoose(boolean isChoose) {
        this.isChoose = isChoose;
    }
}
