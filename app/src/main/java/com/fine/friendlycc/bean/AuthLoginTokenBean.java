package com.fine.friendlycc.bean;


import com.google.gson.annotations.SerializedName;

/**
 * @author wulei
 */
public class AuthLoginTokenBean {

    private String token;
    @SerializedName("bind_phone")
    private Integer bindPhone;

    private String userID;
    private String userSig;
    @SerializedName("is_contract")
    private int isContract;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getBindPhone() {
        return bindPhone;
    }

    public void setBindPhone(Integer bindPhone) {
        this.bindPhone = bindPhone;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserSig() {
        return userSig;
    }

    public void setUserSig(String userSig) {
        this.userSig = userSig;
    }

    public int getIsContract() {
        return isContract;
    }

    public void setIsContract(int isContract) {
        this.isContract = isContract;
    }
}