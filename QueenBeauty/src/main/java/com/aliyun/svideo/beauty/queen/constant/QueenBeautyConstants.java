package com.aliyun.svideo.beauty.queen.constant;

import android.annotation.SuppressLint;

import com.aliyun.svideo.beauty.queen.bean.QueenBeautyFaceParams;
import com.aliyun.svideo.beauty.queen.bean.QueenBeautyShapeParams;

import java.util.HashMap;
import java.util.Map;


public class QueenBeautyConstants {

    //美颜磨皮
    public final static int BUFFING = 0;
    //美颜美白
    public final static int WHITENING = 1;
    //美颜锐化
    public final static int SHARPEN = 2;

    //美肌自定义
    public final static int SHAPE_CUSTOM_FACE = 0;
    //美肌优雅
    public final static int SHAPE_GRACE_FACE = 1;
    //美肌精致
    public final static int SHAPE_FINE_FACE = 2;
    //美肌网红
    public final static int SHAPE_CELEBRITY_FACE = 3;
    //美肌可爱
    public final static int SHAPE_LOVELY_FACE = 4;
    //美肌婴儿
    public final static int SHAPE_BABY_FACE = 5;

    /**
     * ******************************************
     * 美肌
     * CUT_FACE:     窄脸
     * THIN_FACE:   瘦脸
     * LONG_FACE:    脸长
     * LOWER_JAW:    缩下巴
     * BIG_EYE:     大眼
     * THIN_NOSE:   瘦鼻
     * MOUTH_WIDTH:    唇宽
     * THIN_MANDIBLE:   下颌
     * CUT_CHEEK:    颧骨
     * ******************************************
     */
    public final static int CUT_FACE = 0;
    public final static int THIN_FACE = 1;
    public final static int LONG_FACE = 2;
    public final static int LOWER_JAW = 3;
    public final static int BIG_EYE = 4;
    public final static int THIN_NOSE = 5;
    public final static int MOUTH_WIDTH = 6;
    public final static int THIN_MANDIBLE = 7;
    public final static int CUT_CHEEK = 8;

    @SuppressLint("UseSparseArrays")
    public final static Map<Integer, QueenBeautyFaceParams> BEAUTY_MAP = new HashMap<>();
    @SuppressLint("UseSparseArrays")
    public final static Map<Integer, QueenBeautyShapeParams> BEAUTY_SHAPE_DEFAULT_MAP = new HashMap<>();

    static {
        final QueenBeautyFaceParams beautyParams0 = new QueenBeautyFaceParams();
        beautyParams0.beautyBuffing = 0;
        beautyParams0.beautyWhite = 0;
        beautyParams0.beautySharpen = 0;
        beautyParams0.beautySlimFace = 0;
        beautyParams0.beautyBigEye = 0;
        beautyParams0.beautyLevel = 0;

        // 1级: 磨皮 = 12, 美白 = 20, 锐化 = 20
        final QueenBeautyFaceParams beautyParams1 = new QueenBeautyFaceParams();
        beautyParams1.beautyBuffing = 12;
        beautyParams1.beautyWhite = 20;
        beautyParams1.beautySharpen = 10;
        beautyParams1.beautySlimFace = 20;
        beautyParams1.beautyBigEye = 20;
        beautyParams1.beautyLevel = 1;

        // 2级: 磨皮 = 24, 美白 = 40, 锐化 = 40
        final QueenBeautyFaceParams beautyParams2 = new QueenBeautyFaceParams();
        beautyParams2.beautyBuffing = 24;
        beautyParams2.beautyWhite = 40;
        beautyParams2.beautySharpen = 10;
        beautyParams2.beautySlimFace = 40;
        beautyParams2.beautyBigEye = 40;
        beautyParams2.beautyLevel = 2;

        // 3级: 磨皮 = 36, 美白 = 60, 锐化 = 60
        final QueenBeautyFaceParams beautyParams3 = new QueenBeautyFaceParams();
        beautyParams3.beautyBuffing = 36;
        beautyParams3.beautyWhite = 60;
        beautyParams3.beautySharpen = 10;
        beautyParams3.beautySlimFace = 60;
        beautyParams3.beautyBigEye = 60;
        beautyParams3.beautyLevel = 3;

        // 4级: 磨皮 = 48, 美白 = 80, 锐化 = 80
        final QueenBeautyFaceParams beautyParams4 = new QueenBeautyFaceParams();
        beautyParams4.beautyBuffing = 48;
        beautyParams4.beautyWhite = 80;
        beautyParams4.beautySharpen = 10;
        beautyParams4.beautySlimFace = 80;
        beautyParams4.beautyBigEye = 80;
        beautyParams4.beautyLevel = 4;

        // 5级: 磨皮 = 60, 美白 = 100, 锐化 = 100
        final QueenBeautyFaceParams beautyParams5 = new QueenBeautyFaceParams();
        beautyParams5.beautyBuffing = 60;
        beautyParams5.beautyWhite = 100;
        beautyParams5.beautySharpen = 10;
        beautyParams4.beautySlimFace = 100;
        beautyParams4.beautyBigEye = 100;
        beautyParams5.beautyLevel = 5;

        BEAUTY_MAP.put(0, beautyParams0);
        BEAUTY_MAP.put(1, beautyParams1);
        BEAUTY_MAP.put(2, beautyParams2);
        BEAUTY_MAP.put(3, beautyParams3);
        BEAUTY_MAP.put(4, beautyParams4);
        BEAUTY_MAP.put(5, beautyParams5);

    }

    static {
        final QueenBeautyShapeParams beautyParams0 = new QueenBeautyShapeParams();
        /**
         * 自定义
         */
        beautyParams0.beautyBigEye = 4;
        beautyParams0.beautyLongFace = 4;
        beautyParams0.beautyCutFace = 9;
        beautyParams0.beautyThinFace = 7;
        beautyParams0.beautyLowerJaw = 17;
        beautyParams0.beautyMouthWidth = 20;
        beautyParams0.beautyThinNose = 0;
        beautyParams0.beautyThinMandible = 0;
        beautyParams0.beautyCutCheek = 0;
        beautyParams0.shapeType = SHAPE_CUSTOM_FACE;

        /**
         * 优雅
         */
        final QueenBeautyShapeParams beautyParams1 = new QueenBeautyShapeParams();
        beautyParams1.beautyBigEye = 22;
        beautyParams1.beautyLongFace = 17;
        beautyParams1.beautyCutFace = 33;
        beautyParams1.beautyThinFace = 33;
        beautyParams1.beautyLowerJaw = 7;
        beautyParams1.beautyMouthWidth = 18;
        beautyParams1.beautyThinNose = 0;
        beautyParams1.beautyThinMandible = 0;
        beautyParams1.beautyCutCheek = 0;
        beautyParams1.shapeType = SHAPE_GRACE_FACE;


        /**
         * 精致
         */
        final QueenBeautyShapeParams beautyParams2 = new QueenBeautyShapeParams();
        beautyParams2.beautyBigEye = 0;
        beautyParams2.beautyLongFace = 10;
        beautyParams2.beautyCutFace = 6;
        beautyParams2.beautyThinFace = 33;
        beautyParams2.beautyLowerJaw = 33;
        beautyParams2.beautyMouthWidth = 0;
        beautyParams2.beautyThinNose = 0;
        beautyParams2.beautyThinMandible = 0;
        beautyParams2.beautyCutCheek = 0;
        beautyParams2.shapeType = SHAPE_FINE_FACE;


        /**
         * 网红
         */
        final QueenBeautyShapeParams beautyParams3 = new QueenBeautyShapeParams();
        beautyParams3.beautyBigEye = 16;
        beautyParams3.beautyLongFace = 2;
        beautyParams3.beautyCutFace = 33;
        beautyParams3.beautyThinFace = 9;
        beautyParams3.beautyLowerJaw = 2;
        beautyParams3.beautyMouthWidth = 12;
        beautyParams3.beautyThinNose = 0;
        beautyParams3.beautyThinMandible = 0;
        beautyParams3.beautyCutCheek = 0;
        beautyParams3.shapeType = SHAPE_CELEBRITY_FACE;


        /**
         * 可爱
         */

        final QueenBeautyShapeParams beautyParams4 = new QueenBeautyShapeParams();
        beautyParams4.beautyBigEye = 33;
        beautyParams4.beautyLongFace = 16;
        beautyParams4.beautyCutFace = 14;
        beautyParams4.beautyThinFace = 33;
        beautyParams4.beautyLowerJaw = -3;
        beautyParams4.beautyMouthWidth = -8;
        beautyParams4.beautyThinNose = 0;
        beautyParams4.beautyThinMandible = 0;
        beautyParams4.beautyCutCheek = 0;
        beautyParams4.shapeType = SHAPE_LOVELY_FACE;


        /**
         * 婴儿
         */
        final QueenBeautyShapeParams beautyParams5 = new QueenBeautyShapeParams();
        beautyParams5.beautyBigEye = 16;
        beautyParams5.beautyLongFace = 27;
        beautyParams5.beautyCutFace = 15;
        beautyParams5.beautyThinFace = 9;
        beautyParams5.beautyLowerJaw = -10;
        beautyParams5.beautyMouthWidth = -8;
        beautyParams5.beautyThinNose = 0;
        beautyParams5.beautyThinMandible = 0;
        beautyParams5.beautyCutCheek = 0;
        beautyParams5.shapeType = SHAPE_BABY_FACE;


        BEAUTY_SHAPE_DEFAULT_MAP.put(0, beautyParams0);
        BEAUTY_SHAPE_DEFAULT_MAP.put(1, beautyParams1);
        BEAUTY_SHAPE_DEFAULT_MAP.put(2, beautyParams2);
        BEAUTY_SHAPE_DEFAULT_MAP.put(3, beautyParams3);
        BEAUTY_SHAPE_DEFAULT_MAP.put(4, beautyParams4);
        BEAUTY_SHAPE_DEFAULT_MAP.put(5, beautyParams5);

    }

}
