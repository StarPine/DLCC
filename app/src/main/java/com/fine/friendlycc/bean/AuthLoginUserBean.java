package com.fine.friendlycc.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Author: 彭石林
 * Time: 2022/1/12 18:23
 * Description: This is AuthLoginUserBean
 */
public class AuthLoginUserBean {
    private String token;
    private String userID;
    private String userSig;
    @SerializedName("is_contract")
    private int isContract;

    @SerializedName("is_new_user")
    private Integer isNewUser;
    @SerializedName("is_bind_game")
    private Integer isBindGame;


    //第三方类型登录 1：facebook 2：google
    private Integer typeLogin =-1;
    //第三方 userId
    private String authUserId;

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

    public Integer getIsNewUser() {
        return isNewUser;
    }

    public void setIsNewUser(Integer isNewUser) {
        this.isNewUser = isNewUser;
    }

    public Integer getTypeLogin() {
        return typeLogin;
    }

    public void setTypeLogin(Integer typeLogin) {
        this.typeLogin = typeLogin;
    }

    public String getAuthUserId() {
        return authUserId;
    }

    public void setAuthUserId(String authUserId) {
        this.authUserId = authUserId;
    }

    public Integer getIsBindGame() {
        return isBindGame;
    }

    public void setIsBindGame(Integer isBindGame) {
        this.isBindGame = isBindGame;
    }
}