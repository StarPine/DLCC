package com.fine.friendlycc.api;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Author: 彭石林
 * Time: 2022/3/29 17:38
 * Description: This is PlayFunAuthUserEntity
 */
public class PlayFunAuthUserEntity implements Serializable {
    private String authName;
    private String authEmail ;
    //第三方 userId
    private String authTokenUserId;
    private String authAccessToken;
    private Integer authSex;
    private String authPhone;
    //第三方类型登录 1：facebook 2：google
    private Integer typeLogin =-1;

    private String token;
    private String userID;
    private String userSig;
    @SerializedName("is_contract")
    private int isContract;

    @SerializedName("is_new_user")
    private Integer isNewUser;
    @SerializedName("is_bind_game")
    private Integer isBindGame;

    public PlayFunAuthUserEntity() {
    }

    public String getAuthName() {
        return authName;
    }

    public void setAuthName(String authName) {
        this.authName = authName;
    }

    public String getAuthEmail() {
        return authEmail;
    }

    public void setAuthEmail(String authEmail) {
        this.authEmail = authEmail;
    }

    public String getAuthTokenUserId() {
        return authTokenUserId;
    }

    public void setAuthTokenUserId(String authTokenUserId) {
        this.authTokenUserId = authTokenUserId;
    }

    public String getAuthAccessToken() {
        return authAccessToken;
    }

    public void setAuthAccessToken(String authAccessToken) {
        this.authAccessToken = authAccessToken;
    }

    public Integer getAuthSex() {
        return authSex;
    }

    public void setAuthSex(Integer authSex) {
        this.authSex = authSex;
    }

    public String getAuthPhone() {
        return authPhone;
    }

    public void setAuthPhone(String authPhone) {
        this.authPhone = authPhone;
    }

    public Integer getTypeLogin() {
        return typeLogin;
    }

    public void setTypeLogin(Integer typeLogin) {
        this.typeLogin = typeLogin;
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

    public Integer getIsNewUser() {
        return isNewUser;
    }

    public void setIsNewUser(Integer isNewUser) {
        this.isNewUser = isNewUser;
    }

    public Integer getIsBindGame() {
        return isBindGame;
    }

    public void setIsBindGame(Integer isBindGame) {
        this.isBindGame = isBindGame;
    }

    @Override
    public String toString() {
        return "PlayFunAuthUserEntity{" +
                "authName='" + authName + '\'' +
                ", authEmail='" + authEmail + '\'' +
                ", authTokenUserId='" + authTokenUserId + '\'' +
                ", authAccessToken='" + authAccessToken + '\'' +
                ", authSex=" + authSex +
                ", authPhone='" + authPhone + '\'' +
                ", typeLogin=" + typeLogin +
                ", token='" + token + '\'' +
                ", userID='" + userID + '\'' +
                ", userSig='" + userSig + '\'' +
                ", isContract=" + isContract +
                ", isNewUser=" + isNewUser +
                ", isBindGame=" + isBindGame +
                '}';
    }
}
