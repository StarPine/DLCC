package com.faceunity.nama.entity;

import java.io.Serializable;

/**
 * @Name： PlayChat_Google
 * @Description：
 * @Author： liaosf
 * @Date： 2022/4/2 12:10
 * 修改备注：
 */
public class FaceBeautyLocalBean implements Serializable {

    private String key;//名称标识
    private double value;//数值

    public FaceBeautyLocalBean(String key, double value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "FaceBeautyLocalBean{" +
                "key='" + key + '\'' +
                ", value=" + value +
                '}';
    }
}
