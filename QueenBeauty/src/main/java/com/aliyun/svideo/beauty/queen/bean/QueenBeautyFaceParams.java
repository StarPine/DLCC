package com.aliyun.svideo.beauty.queen.bean;

/**
 * Created by Akira on 2018/6/20.
 */
public class QueenBeautyFaceParams implements Cloneable {

    /**
     * 磨皮
     */
    public float beautyBuffing = 0;
    /**
     * 美白
     */
    public float beautyWhite = 0;


    /**
     * 锐化
     */
    public float beautySharpen = 0;

    /**
     * 瘦脸
     */
    public float beautySlimFace = 0;
    /**
     * 大眼
     */
    public float beautyBigEye = 0;
    /**
     * 美颜等级
     */
    public int beautyLevel = 0;

    @Override
    public QueenBeautyFaceParams clone() {
        QueenBeautyFaceParams beautyParams = null;
        try {
            beautyParams = (QueenBeautyFaceParams) super.clone();
            beautyParams.beautyLevel = this.beautyLevel;
            beautyParams.beautyWhite = this.beautyWhite;
            beautyParams.beautyBuffing = this.beautyBuffing;
            beautyParams.beautySharpen = this.beautySharpen;
            beautyParams.beautySlimFace = this.beautySlimFace;
            beautyParams.beautyBigEye = this.beautyBigEye;
            return beautyParams;

        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
