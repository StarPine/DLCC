package com.fine.friendlycc.bean;

/**
 * Author: 彭石林
 * Time: 2021/8/13 17:00
 * Description: This is AddressCityItemBean
 */
public class AddressCityItemBean {
    private boolean isChoose = false;
    private String postalCode;
    private String region;

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public boolean getIsChoose() {
        return isChoose;
    }

    public void setIsChoose(boolean isChoose) {
        this.isChoose = isChoose;
    }
}