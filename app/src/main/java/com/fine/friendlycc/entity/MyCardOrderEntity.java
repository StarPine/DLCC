package com.fine.friendlycc.entity;

import com.google.gson.annotations.SerializedName;

/**
 * @author wulei
 */
public class MyCardOrderEntity {
    @SerializedName("AuthCode")
    private String authCode;

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }
}
