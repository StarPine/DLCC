package com.aliyun.svideo.beauty.queen.bean;

import com.aliyun.svideo.beauty.queen.constant.QueenBeautyConstants;

/**
 * Created by Akira on 2018/6/20.
 */
public class QueenBeautyShapeParams implements Cloneable {

    /**
     * 颧骨[0,1]
     */
    public float beautyCutCheek = 0;
    /**
     * 窄脸，削脸[0,1]
     */
    public float beautyCutFace = 0;
    /**
     * 瘦脸[0,1]
     */
    public float beautyThinFace = 0;
    /**
     * 脸长[0,1]
     */
    public float beautyLongFace = 0;
    /**
     * 下巴缩短[-1,1]
     */
    public float beautyLowerJaw = 0;
    /**
     * 下巴拉长[-1,1]
     */
    public float beautyHigherJaw = 0;
    /**
     * 瘦下巴[0,1]
     */
    public float beautyThinJaw = 0;
    /**
     * 瘦下颌[0,1]
     */
    public float beautyThinMandible = 0;
    /**
     * 大眼[0,1]
     */
    public float beautyBigEye = 0;
    /**
     * 眼角1[0,1]
     */
    public float beautyEyeAngle1 = 0;
    /**
     * 眼距[-1,1]
     */
    public float beautyCanthus = 0;
    /**
     * 拉宽眼距[-1,1]
     */
    public float beautyCanthus1 = 0;
    /**
     * 眼角2[-1,1]
     */
    public float beautyEyeAngle2 = 0;
    /**
     * 眼睛高度[-1,1]
     */
    public float beautyEyeTDAngle = 0;
    /**
     * 瘦鼻[0,1]
     */
    public float beautyThinNose = 0;
    /**
     * 鼻翼[0,1]
     */
    public float beautyNosewing = 0;
    /**
     * 鼻长[-1,1]
     */
    public float beautyNasalHeight = 0;
    /**
     * 鼻头长[-1,1]
     */
    public float beautyNoseTipHeight = 0;
    /**
     * 唇宽[-1,1]
     */
    public float beautyMouthWidth = 0;
    /**
     * 嘴唇大小[-1,1]
     */
    public float beautyMouthSize = 0;
    /**
     * 唇高[-1,1]
     */
    public float beautyMouthHigh = 0;
    /**
     * 人中[-1,1]
     */
    public float beautyPhiltrum = 0;
    /**
     * 美型参数：发际线
     * 参数范围：[-1,1]
     */
    public float beautyHairLine = 0;
    /**
     * 美型参数：嘴角上扬（微笑）
     * 参数范围：[-1,1]
     */
    public float beautySmile = 0;


    /**
     * 美肌分类
     */
    public int shapeType = QueenBeautyConstants.SHAPE_CUSTOM_FACE;

    public QueenBeautyShapeParams() {
    }

    @Override
    public String toString() {
        return "QueenBeautyShapeParams{" +
                "beautyCutCheek=" + beautyCutCheek +
                ", beautyCutFace=" + beautyCutFace +
                ", beautyThinFace=" + beautyThinFace +
                ", beautyLongFace=" + beautyLongFace +
                ", beautyLowerJaw=" + beautyLowerJaw +
                ", beautyHigherJaw=" + beautyHigherJaw +
                ", beautyThinJaw=" + beautyThinJaw +
                ", beautyThinMandible=" + beautyThinMandible +
                ", beautyBigEye=" + beautyBigEye +
                ", beautyEyeAngle1=" + beautyEyeAngle1 +
                ", beautyCanthus=" + beautyCanthus +
                ", beautyCanthus1=" + beautyCanthus1 +
                ", beautyEyeAngle2=" + beautyEyeAngle2 +
                ", beautyEyeTDAngle=" + beautyEyeTDAngle +
                ", beautyThinNose=" + beautyThinNose +
                ", beautyNosewing=" + beautyNosewing +
                ", beautyNasalHeight=" + beautyNasalHeight +
                ", beautyNoseTipHeight=" + beautyNoseTipHeight +
                ", beautyMouthWidth=" + beautyMouthWidth +
                ", beautyMouthSize=" + beautyMouthSize +
                ", beautyHairLine=" + beautyHairLine +
                ", beautySmile=" + beautySmile +
                ", shapeType=" + shapeType +
                '}';
    }

    @Override
    public QueenBeautyShapeParams clone() {
        QueenBeautyShapeParams beautyParams = null;
        try {
            beautyParams = (QueenBeautyShapeParams)super.clone();
            beautyParams.beautyCutCheek = this.beautyCutCheek;
            beautyParams.beautyCutFace = this.beautyCutFace;
            beautyParams.beautyThinFace = this.beautyThinFace;
            beautyParams.beautyLongFace = this.beautyLongFace;
            beautyParams.beautyLowerJaw = this.beautyLowerJaw;
            beautyParams.beautyHigherJaw = this.beautyHigherJaw;
            beautyParams.beautyThinJaw = this.beautyThinJaw;
            beautyParams.beautyThinMandible = this.beautyThinMandible;
            beautyParams.beautyBigEye = this.beautyBigEye;
            beautyParams.beautyEyeAngle1 = this.beautyEyeAngle1;
            beautyParams.beautyCanthus = this.beautyCanthus;
            beautyParams.beautyCanthus1 = this.beautyCanthus1;
            beautyParams.beautyEyeAngle2 = this.beautyEyeAngle2;
            beautyParams.beautyEyeTDAngle = this.beautyEyeTDAngle;
            beautyParams.beautyThinNose = this.beautyThinNose;
            beautyParams.beautyNosewing = this.beautyNosewing;
            beautyParams.beautyNasalHeight = this.beautyNasalHeight;
            beautyParams.beautyNoseTipHeight = this.beautyNoseTipHeight;
            beautyParams.beautyMouthWidth = this.beautyMouthWidth;
            beautyParams.beautyMouthSize = this.beautyMouthSize;
            beautyParams.beautyMouthHigh = this.beautyMouthHigh;
            beautyParams.beautyPhiltrum = this.beautyPhiltrum;
            beautyParams.beautyHairLine = this.beautyHairLine;
            beautyParams.beautySmile = this.beautySmile;
            return beautyParams;

        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
