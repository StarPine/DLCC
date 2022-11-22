package com.fine.friendlycc.bean;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * @author wulei
 */
public class TokenBean implements Serializable {

    private String token;
    private String userID;
    private String userSig;
    @SerializedName("is_contract")
    private int isContract;

    @SerializedName("is_new_user")
    private Integer isNewUser;

    public TokenBean() {
    }

    public TokenBean(String token, String userID, String userSig, int isContract) {
        this.token = token;
        this.userID = userID;
        this.userSig = userSig;
        this.isContract = isContract;
    }

    public Integer getIsNewUser() {
        return isNewUser;
    }

    public void setIsNewUser(Integer isNewUser) {
        this.isNewUser = isNewUser;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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