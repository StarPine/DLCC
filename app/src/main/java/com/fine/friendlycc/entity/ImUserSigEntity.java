package com.fine.friendlycc.entity;

import java.io.Serializable;

/**
 * 修改备注：
 *
 * @Name： PlayFun_Google
 * @Description：
 * @Author： liaosf
 * @Date： 2022/9/9 16:55
 */
public class ImUserSigEntity implements Serializable {
    private String userSig;

    public String getUserSig() {
        return userSig;
    }

    public void setUserSig(String userSig) {
        this.userSig = userSig;
    }
}
