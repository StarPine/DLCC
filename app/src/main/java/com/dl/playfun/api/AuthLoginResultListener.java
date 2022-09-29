package com.dl.playfun.api;

/**
 * Author: 彭石林
 * Time: 2022/3/30 14:11
 * Description: This is AuthLoginResultListener
 */
public interface AuthLoginResultListener {
    void authLoginSuccess(PlayFunAuthUserEntity playFunAuthUserEntity);
    void authLoginError(int code,int type,String message);
}
