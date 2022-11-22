package com.fine.friendlycc.bean;

import java.io.Serializable;

/**
 * 修改备注：
 *
 * @Name： PlayCC
 * @Description：
 * @Author： liaosf
 * @Date： 2022/9/9 16:55
 */
public class ImUserSigBean implements Serializable {
    private String userSig;

    public String getUserSig() {
        return userSig;
    }

    public void setUserSig(String userSig) {
        this.userSig = userSig;
    }
}