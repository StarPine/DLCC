package com.aliyun.svideo.beauty.queen.inteface;

import com.aliyun.svideo.beauty.queen.bean.QueenBeautyFaceParams;
import com.aliyun.svideo.beauty.queen.bean.QueenBeautyShapeParams;

/**
 * Created by Akira on 2018/5/31.
 */

public interface OnBeautyParamsChangeListener {

    void onBeautyFaceChange(QueenBeautyFaceParams param);

    void onBeautyShapeChange(QueenBeautyShapeParams param);
}
