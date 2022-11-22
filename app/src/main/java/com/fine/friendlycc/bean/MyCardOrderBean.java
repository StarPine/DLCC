package com.fine.friendlycc.bean;

import com.google.gson.annotations.SerializedName;

/**
 * @author wulei
 */
public class MyCardOrderBean {
    @SerializedName("AuthCode")
    private String authCode;

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }
}