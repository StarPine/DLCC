package com.fine.friendlycc.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * @Name： PlayCC
 * @Description：
 * @Author： liaosf
 * @Date： 2022/8/3 10:58
 * 修改备注：
 */
public class CrystalDetailsConfigBean implements Serializable {

    /**
     * crystalDetailsConfig : {"maleIsShow":0,"femaleIsShow":1}
     */

    @SerializedName("maleIsShow")
    private int maleIsShow;
    @SerializedName("femaleIsShow")
    private int femaleIsShow;

    public int getMaleIsShow() {
        return maleIsShow;
    }

    public int getFemaleIsShow() {
        return femaleIsShow;
    }
}