package com.fine.friendlycc.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Author: 彭石林
 * Time: 2022/4/2 14:33
 * Description: IM userid 转换
 */
public class IMTransUserBean {
    @SerializedName("user_id")
    private Integer userId;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}